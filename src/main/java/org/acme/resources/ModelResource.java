package org.acme.resources;

import org.acme.dtos.model.ModelRequestDTO;
import org.acme.dtos.model.ModelResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.model.ModelService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

@Path("/model")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    ModelService modelService;

    @GET
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<ModelResponseDTO> models = modelService.findAllModels(pagination);

        return Response.ok(models)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", models.total())
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
        PaginationResponseDTO<ModelResponseDTO> models = modelService.findModelByName(name, pagination);

        return Response.ok(models)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", models.total())
                .build();
    }

    @GET
    @Path("/manufacturer/{manufacturerId}")
    public Response findByManufacturer(
        @PathParam("manufacturerId") String manufacturerId,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<ModelResponseDTO> models = modelService.findModelByManufacturer(manufacturerId, pagination);

        return Response.ok(models)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", models.total())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        return modelService.findModelById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid ModelRequestDTO dto) {
        ModelResponseDTO created = modelService.createModel(dto);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid ModelRequestDTO dto) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        ModelResponseDTO updated = modelService.updateModel(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        Integer deleted = modelService.deleteModel(id);
        return (deleted == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
