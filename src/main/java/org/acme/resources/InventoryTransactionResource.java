package org.acme.resources;

import java.time.LocalDateTime;

import org.acme.dtos.inventory.InventoryTransactionRequestDTO;
import org.acme.dtos.inventory.InventoryTransactionResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.inventory.InventoryTransactionService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryTransactionResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    InventoryTransactionService transactionService;

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<InventoryTransactionResponseDTO> list = transactionService.findAllTransactions(pagination);
        return Response.ok(list).build();
    }

    @GET
    @Path("/gpu/{gpuId}")
    @RolesAllowed("ADMIN")
    public Response findByGpu(
        @PathParam("gpuId") String gpuId,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(transactionService.findByGpu(gpuId, pagination)).build();
    }

    @GET
    @Path("/type/{type}")
    @RolesAllowed("ADMIN")
    public Response findByType(
        @PathParam("type") String type,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(transactionService.findByTransactionType(type, pagination)).build();
    }

    @GET
    @Path("/date-range")
    @RolesAllowed("ADMIN")
    public Response findByDateRange(
        @QueryParam("start") String start,
        @QueryParam("end") String end,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        LocalDateTime startDate = start != null ? LocalDateTime.parse(start) : LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = end != null ? LocalDateTime.parse(end) : LocalDateTime.now();

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        return Response.ok(transactionService.findByDateRange(startDate, endDate, pagination)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response findById(@PathParam("id") String id) {
        return transactionService.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid InventoryTransactionRequestDTO dto) {
        InventoryTransactionResponseDTO created = transactionService.createTransaction(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") String id) {
        return transactionService.deleteTransaction(id) == 1
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
