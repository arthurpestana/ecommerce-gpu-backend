package org.acme.resources;

import org.acme.dtos.user.UserRequestDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.user.UserService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    UserService userService;

    @GET
    public Response findAll(
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        PaginationResponseDTO<UserResponseDTO> users = userService.findAllUsers(pagination);

        return Response.ok(users)
                .header("X-Offset", offset)
                .header("X-Limit", limit)
                .header("X-Total-Count", users.total())
                .build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(
        @PathParam("name") String name,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        PaginationResponseDTO<UserResponseDTO> users = userService.findUserByName(name, pagination);

        return Response.ok(users)
                .header("X-Offset", offset)
                .header("X-Limit", limit)
                .header("X-Total-Count", users.total())
                .build();
    }

    @GET
    @Path("/email/{email}")
    public Response findByEmail(@PathParam("email") String email) {
        if (email == null || email.isBlank()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("E-mail inválido fornecido.")
                    .build();
        }

        UserResponseDTO user = userService.findUserByEmail(email);
        return Response.ok(user).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("ID inválido fornecido.")
                    .build();
        }

        return userService.findUserById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid UserRequestDTO dto) {
        UserResponseDTO createdUser = userService.createUser(dto);

        return Response.status(Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid UserRequestDTO dto) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("ID inválido fornecido.")
                    .build();
        }

        UserResponseDTO updatedUser = userService.updateUser(id, dto);
        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("/{id}")
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
