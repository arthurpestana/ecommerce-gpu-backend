package org.acme.resources;

import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.dtos.user.UserRequestDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.dtos.user.UserStatusRequestDTO;
import org.acme.services.user.UserService;
import org.acme.utils.AuthenticatedUser;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    UserService userService;

    @Inject
    AuthenticatedUser auth;

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<UserResponseDTO> users = userService.findAllUsers(pagination);

        return Response.ok(users)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", users.total())
                .build();
    }

    @GET
    @Path("/name/{name}")
    @RolesAllowed("ADMIN")
    public Response findByName(
            @PathParam("name") String name,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<UserResponseDTO> users = userService.findUserByName(name, pagination);

        return Response.ok(users)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", users.total())
                .build();
    }

    @GET
    @Path("/email/{email}")
    @RolesAllowed({ "ADMIN", "CUSTOMER" })
    public Response findByEmail(@PathParam("email") String email) {
        if (email == null || email.isBlank()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("E-mail inválido fornecido.")
                    .build();
        }

        if (!auth.isAdmin() && !email.equalsIgnoreCase(userService.findUserById(auth.getUserId()).get().email())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode acessar informações de outro usuário.")
                    .build();
        }

        UserResponseDTO user = userService.findUserByEmail(email);
        return Response.ok(user).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "ADMIN", "CUSTOMER" })
    public Response findById(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("ID inválido fornecido.")
                    .build();
        }

        if (!auth.isAdmin() && !id.equals(auth.getUserId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode acessar dados de outros usuários.")
                    .build();
        }
        
        return userService.findUserById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid UserRequestDTO dto) {
        UserResponseDTO createdUser = userService.createUser(dto);

        return Response.status(Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response update(@PathParam("id") String id, @Valid UserRequestDTO dto) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("ID inválido fornecido.")
                    .build();
        }

        if (!auth.isAdmin() && !id.equals(auth.getUserId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode editar outro usuário.")
                    .build();
        }

        UserResponseDTO updatedUser = userService.updateUser(id, dto);
        return Response.ok(updatedUser).build();
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("ADMIN")
    public Response activate(@PathParam("id") String id, @Valid UserStatusRequestDTO isActive) {
        return Response.ok(userService.setActiveStatus(id, isActive)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("ID inválido fornecido.")
                    .build();
        }

        Integer deleted = userService.deleteUser(id);
        return (deleted == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
