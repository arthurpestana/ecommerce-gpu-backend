package org.acme.resources;

import org.acme.dtos.address.AddressRequestDTO;
import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.address.AddressService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;


@Path("/address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    AddressService addressService;

    @GET
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
    public Response findByUser(
        @PathParam("userId") String userId,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<AddressResponseDTO> addresses = addressService.findAddressByUser(userId, pagination);
        return Response.ok(addresses).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        return addressService.findAddressById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid AddressRequestDTO dto) {
        AddressResponseDTO created = addressService.createAddress(dto);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid AddressRequestDTO dto) {
        AddressResponseDTO updated = addressService.updateAddress(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Integer deleted = addressService.deleteAddress(id);
        return (deleted == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
