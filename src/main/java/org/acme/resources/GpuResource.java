package org.acme.resources;

import java.math.BigDecimal;

import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.gpu.GpuService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/gpu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GpuResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    GpuService gpuService;

    @GET
    public Response findAll(
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        PaginationResponseDTO<GpuResponseDTO> gpus = gpuService.findAllGpus(pagination);
        return Response.ok(gpus).build();
    }

    @GET
    @Path("/filter")
    public Response filter(
        @QueryParam("name") String name,
        @QueryParam("modelId") Long modelId,
        @QueryParam("manufacturerId") Long manufacturerId,
        @QueryParam("minPrice") BigDecimal minPrice,
        @QueryParam("maxPrice") BigDecimal maxPrice,
        @QueryParam("isActive") Boolean isActive,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {

        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        var gpus = gpuService.findFiltered(name, modelId, manufacturerId, minPrice, maxPrice, isActive, pagination);
        return Response.ok(gpus).build();
    }

    @GET
    @Path("/model/{modelId}")
    public Response findByModel(
        @PathParam("modelId") Long modelId,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByModel(modelId, pagination)).build();
    }

    @GET
    @Path("/manufacturer/{manufacturerId}")
    public Response findByManufacturer(
        @PathParam("manufacturerId") Long manufacturerId,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByManufacturer(manufacturerId, pagination)).build();
    }

    @GET
    @Path("/price-range")
    public Response findByPriceRange(
        @QueryParam("min") BigDecimal min,
        @QueryParam("max") BigDecimal max,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByPriceRange(min, max, pagination)).build();
    }

    @GET
    @Path("/technology/{tech}")
    public Response findByTechnology(@PathParam("tech") String tech,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByTechnology(tech, pagination)).build();
    }

    @GET
    @Path("/category/{category}")
    public Response findByCategory(
        @PathParam("category") String category,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByCategory(category, pagination)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return gpuService.findGpuById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid GpuRequestDTO dto) {
        return Response.status(Response.Status.CREATED).entity(gpuService.createGpu(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid GpuRequestDTO dto) {
        return Response.ok(gpuService.updateGpu(id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return gpuService.deleteGpu(id) == 1
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
