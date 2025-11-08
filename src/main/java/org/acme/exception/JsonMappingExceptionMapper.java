package org.acme.exception;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Context
    UriInfo uriInfo;

    @ConfigProperty(name = "problem.base-url")
    String baseUrl;

    @Override
    public Response toResponse(JsonMappingException e) {
        
        String field = e.getPath().stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));

        if (field == null || field.isBlank()) {
            field = "unknown";
        }

        String detail = String.format("Formato de dado inválido para o campo '%s'. Verifique o valor enviado.", field);
        Problem.FieldError fieldError = new Problem.FieldError(field, "Formato de dado inválido.");

        Problem problem = new Problem();
        problem.type = baseUrl + "/errors/invalid-format";
        problem.title = "Formato de dados inválido";
        problem.status = Response.Status.BAD_REQUEST.getStatusCode();
        problem.detail = detail;
        problem.instance = (uriInfo != null ? uriInfo.getRequestUri().getPath() : null);
        problem.timestamp = OffsetDateTime.now();
        problem.errors = List.of(fieldError);

        return Response.status(problem.status)
                .type("application/problem+json")
                .entity(problem)
                .build();
    }
}