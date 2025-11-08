package org.acme.exception;

import java.time.OffsetDateTime;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Context
    UriInfo uri;

    @ConfigProperty(name = "problem.base-url")
    String baseUrl;

    @Override
    public Response toResponse(ValidationException e) {
        Problem problem = new Problem();
        problem.type = baseUrl + "/errors/validation-error";
        problem.title = "Erro de validação";
        problem.status = Response.Status.BAD_REQUEST.getStatusCode();
        problem.detail = e.getMessage();
        problem.instance = (uri != null ? uri.getRequestUri().getPath() : null);
        problem.timestamp = OffsetDateTime.now();
        problem.errors = e.getFieldErrors();
        return Response.status(problem.status).type("application/problem+json").entity(problem).build();
    }
}