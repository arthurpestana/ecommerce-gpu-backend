package org.acme.resources;

import java.math.BigDecimal;
import java.util.List;

import org.acme.dtos.gpu.GpuFormRequest;
import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.gpu.GpuStatusRequestDTO;
import org.acme.dtos.image.ImageDeleteManyRequestDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.gpu.GpuService;
import org.acme.services.image.ImageService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<GpuResponseDTO> gpus = gpuService.findAllGpus(pagination);
        return Response.ok(gpus).build();
    }

    @GET
    @Path("/filter")
    public Response filter(
            @QueryParam("name") String name,
            @QueryParam("modelId") String modelId,
            @QueryParam("manufacturerId") String manufacturerId,
            @QueryParam("categoryId") String categoryId,
            @QueryParam("minPrice") BigDecimal minPrice,
            @QueryParam("maxPrice") BigDecimal maxPrice,
            @QueryParam("isActive") Boolean isActive,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);

        return Response.ok(
                gpuService.findFiltered(
                        name,
                        modelId,
                        manufacturerId,
                        minPrice,
                        maxPrice,
                        isActive,
                        categoryId,
                        pagination))
                .build();
    }

    @GET
    @Path("/model/{modelId}")
    public Response findByModel(
            @PathParam("modelId") String modelId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(gpuService.findByModel(modelId, pagination)).build();
    }

    @GET
    @Path("/manufacturer/{manufacturerId}")
    public Response findByManufacturer(
            @PathParam("manufacturerId") String manufacturerId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(gpuService.findByManufacturer(manufacturerId, pagination)).build();
    }

    @GET
    @Path("/price-range")
    public Response findByPriceRange(
            @QueryParam("min") BigDecimal min,
            @QueryParam("max") BigDecimal max,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(gpuService.findByPriceRange(min, max, pagination)).build();
    }

    @GET
    @Path("/technology/{tech}")
    public Response findByTechnology(@PathParam("tech") String tech,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(gpuService.findByTechnology(tech, pagination)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        return gpuService.findGpuById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response create(@Valid GpuFormRequest form) {
        GpuRequestDTO dto = form.data();
        List<FileUpload> imagesUpload = form.images();

        return Response.status(Response.Status.CREATED).entity(gpuService.createGpu(dto, imagesUpload)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") String id, @Valid GpuFormRequest form) {
        GpuRequestDTO dto = form.data();
        List<FileUpload> imagesUpload = form.images();

        return Response.ok(gpuService.updateGpu(id, dto, imagesUpload)).build();
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("ADMIN")
    public Response activate(@PathParam("id") String id, @Valid GpuStatusRequestDTO isActive) {
        return Response.ok(gpuService.setActiveStatus(id, isActive)).build();
    }

    @DELETE
    @Path("/{id}/images")
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteImagesFromGpu(
            @PathParam("id") String gpuId,
            @Valid ImageDeleteManyRequestDTO deleteManyRequestDTO) {
        imageService.deleteManyFromGpu(gpuId, deleteManyRequestDTO.imageIds());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") String id) {
        return gpuService.deleteGpu(id) == 1
                ? Response.ok(1).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
