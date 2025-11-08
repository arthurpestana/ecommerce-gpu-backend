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
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

    @ConfigProperty(name = "problem.base-url")
    String baseUrl;
    
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable t) {

        Problem problem = new Problem();
        problem.type = baseUrl + "/errors/unexpected-error";
        problem.title = "Erro inesperado";
        problem.status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        problem.detail = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
        problem.instance = (uriInfo != null ? uriInfo.getRequestUri().getPath() : null);
        problem.timestamp = OffsetDateTime.now();

        return Response.status(problem.status)
                .type("application/problem+json")
                .entity(problem)
                .build();
    }
}