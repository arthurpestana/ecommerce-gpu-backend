package org.acme.resources;

import java.math.BigDecimal;
import java.util.List;

import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.gpu.GpuStatusRequestDTO;
import org.acme.dtos.gpu.GpuFormRequest;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.gpu.GpuService;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import java.util.UUID;

import org.acme.dtos.image.ImageDeleteManyRequestDTO;
import org.acme.dtos.image.ImageUploadDTO;
import org.acme.services.image.ImageService;
import org.jboss.resteasy.reactive.MultipartForm;

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

@Path("/gpu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GpuResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    GpuService gpuService;

    @Inject
    ImageService imageService;

    @GET
    public Response findAll(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {
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
            @QueryParam("modelId") UUID modelId,
            @QueryParam("manufacturerId") UUID manufacturerId,
            @QueryParam("minPrice") BigDecimal minPrice,
            @QueryParam("maxPrice") BigDecimal maxPrice,
            @QueryParam("isActive") Boolean isActive,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {

        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        var gpus = gpuService.findFiltered(name, modelId, manufacturerId, minPrice, maxPrice, isActive, pagination);
        return Response.ok(gpus).build();
    }

    @GET
    @Path("/model/{modelId}")
    public Response findByModel(
            @PathParam("modelId") UUID modelId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByModel(modelId, pagination)).build();
    }

    @GET
    @Path("/manufacturer/{manufacturerId}")
    public Response findByManufacturer(
            @PathParam("manufacturerId") UUID manufacturerId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByManufacturer(manufacturerId, pagination)).build();
    }

    @GET
    @Path("/price-range")
    public Response findByPriceRange(
            @QueryParam("min") BigDecimal min,
            @QueryParam("max") BigDecimal max,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

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
            @QueryParam("limit") @DefaultValue("10") int limit) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(gpuService.findByCategory(category, pagination)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return gpuService.findGpuById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@Valid GpuFormRequest form) {
        GpuRequestDTO dto = form.data();
        List<FileUpload> imagesUpload = form.images();

        return Response.status(Response.Status.CREATED).entity(gpuService.createGpu(dto, imagesUpload)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response update(@PathParam("id") UUID id, @Valid GpuFormRequest form) {
        GpuRequestDTO dto = form.data();
        List<FileUpload> imagesUpload = form.images();

        return Response.ok(gpuService.updateGpu(id, dto, imagesUpload)).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response activate(@PathParam("id") UUID id, @Valid GpuStatusRequestDTO isActive) {
        return Response.ok(gpuService.setActiveStatus(id, isActive)).build();
    }

    @DELETE
    @Path("/{id}/images")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteImagesFromGpu(
            @PathParam("id") UUID gpuId,
            @Valid ImageDeleteManyRequestDTO deleteManyRequestDTO) {
        imageService.deleteManyFromGpu(gpuId, deleteManyRequestDTO.imageIds());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return gpuService.deleteGpu(id) == 1
                ? Response.ok(1).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
