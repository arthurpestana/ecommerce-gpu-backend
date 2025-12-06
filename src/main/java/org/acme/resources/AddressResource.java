package org.acme.resources;

import org.acme.dtos.address.AddressRequestDTO;
import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.address.AddressService;
import org.acme.utils.AuthenticatedUser;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    AddressService addressService;

    @Inject
    AuthenticatedUser auth;

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<AddressResponseDTO> addresses = addressService.findAllAddresses(pagination);

        return Response.ok(addresses)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", addresses.total())
                .build();
    }

    @GET
    @Path("/city/{city}")
    @RolesAllowed("ADMIN")
    public Response findByCity(
        @PathParam("city") String city,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<AddressResponseDTO> addresses = addressService.findAddressByCity(city, pagination);
        return Response.ok(addresses).build();
    }

    @GET
    @Path("/user/{userId}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response findByUser(
        @PathParam("userId") String userId,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        if (auth.isCustomer() && !auth.getUserId().equals(userId)) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não tem permissão para acessar os endereços de outro usuário.")
                    .build();
        }

        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<AddressResponseDTO> addresses = addressService.findAddressByUser(userId, pagination);
        return Response.ok(addresses).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response findById(@PathParam("id") String id) {
        AddressResponseDTO address = addressService.findAddressById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        if (auth.isCustomer() && !auth.getUserId().equals(address.userId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não tem permissão para acessar esse endereço.")
                    .build();
        }

        return Response.ok(address).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response create(@Valid AddressRequestDTO dto) {
        if (auth.isCustomer() && !auth.getUserId().equals(dto.userId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode criar um endereço para outro usuário.")
                    .build();
        }
        AddressResponseDTO created = addressService.createAddress(dto);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response update(@PathParam("id") String id, @Valid AddressRequestDTO dto) {
        AddressResponseDTO address = addressService.findAddressById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        if (auth.isCustomer() && !auth.getUserId().equals(address.userId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode atualizar um endereço de outro usuário.")
                    .build();
        }

        AddressResponseDTO updated = addressService.updateAddress(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response delete(@PathParam("id") String id) {
        AddressResponseDTO address = addressService.findAddressById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        if (auth.isCustomer() && !auth.getUserId().equals(address.userId())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não pode excluir um endereço de outro usuário.")
                    .build();
        }

        Integer deleted = addressService.deleteAddress(id);
        return (deleted == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
