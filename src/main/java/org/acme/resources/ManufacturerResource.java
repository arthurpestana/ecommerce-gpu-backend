package org.acme.resources;

import org.acme.dtos.manufacturer.ManufacturerRequestDTO;
import org.acme.dtos.manufacturer.ManufacturerResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.manufacturer.ManufacturerService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;


@Path("/manufacturer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManufacturerResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    ManufacturerService manufacturerService;

    @GET
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<ManufacturerResponseDTO> manufacturers = manufacturerService.findAllManufacturers(pagination);

        return Response.ok(manufacturers)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", manufacturers.total())
                .build();
    }

    @GET
    @Path("/name/{name}")
    public Response findByName(
        @PathParam("name") String name,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<ManufacturerResponseDTO> manufacturers = manufacturerService.findManufacturerByName(name, pagination);

        return Response.ok(manufacturers)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", manufacturers.total())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        return manufacturerService.findManufacturerById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid ManufacturerRequestDTO dto) {
        ManufacturerResponseDTO created = manufacturerService.createManufacturer(dto);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") String id, @Valid ManufacturerRequestDTO dto) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        ManufacturerResponseDTO updated = manufacturerService.updateManufacturer(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        Integer deleted = manufacturerService.deleteManufacturer(id);
        return (deleted == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
