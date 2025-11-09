package org.acme.resources;

import org.acme.dtos.category.CategoryRequestDTO;
import org.acme.dtos.category.CategoryResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.category.CategoryService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;


@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    CategoryService categoryService;

    @GET
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit  = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<CategoryResponseDTO> categoriesList = categoryService.findAllCategories(pagination);

        return Response.ok(categoriesList)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", categoriesList.total())
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
        PaginationResponseDTO<CategoryResponseDTO> categoriesList = categoryService.findCategoryByName(name, pagination);

        return Response.ok(categoriesList)
                .header("X-Page", page)
                .header("X-Limit", limit)
                .header("X-Total-Count", categoriesList.total())
                .build();
    }


    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }
        
        return categoryService.findCategoryById(id)
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid CategoryRequestDTO dto) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(dto);

        return Response.status(Status.CREATED)
                .entity(createdCategory)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid CategoryRequestDTO dto) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, dto);
        return Response.ok(updatedCategory).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("ID inválido fornecido.").build();
        }

        Integer deletedCategory = categoryService.deleteCategory(id);
        return (deletedCategory == 1)
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
