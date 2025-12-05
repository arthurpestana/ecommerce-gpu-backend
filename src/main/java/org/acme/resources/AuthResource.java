package org.acme.resources;

import org.acme.dtos.auth.AuthResponseDTO;
import org.acme.dtos.auth.LoginRequestDTO;
import org.acme.dtos.auth.RegisterRequestDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.services.auth.AuthService;
import org.acme.services.user.UserService;
import org.acme.utils.AuthenticatedUser;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @Inject
    AuthenticatedUser auth;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);

        return Response.ok(response).header("Authorization", "Bearer " + response.token()).build();
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequestDTO dto) {
        AuthResponseDTO response = authService.register(dto);

        return Response.status(Status.CREATED)
                .entity(response)
                .header("Authorization", "Bearer " + response.token())
                .build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response me() {
        String userId = auth.getUserId();

        UserResponseDTO user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        return Response.ok(user).build();
    }
}
