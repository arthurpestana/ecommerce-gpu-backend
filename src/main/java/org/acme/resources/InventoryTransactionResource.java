package org.acme.resources;

import java.time.LocalDateTime;

import org.acme.dtos.inventory.InventoryTransactionRequestDTO;
import org.acme.dtos.inventory.InventoryTransactionResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.inventory.InventoryTransactionService;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryTransactionResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    InventoryTransactionService transactionService;

    @GET
    public Response findAll(
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        PaginationResponseDTO<InventoryTransactionResponseDTO> list = transactionService.findAllTransactions(pagination);
        return Response.ok(list).build();
    }

    @GET
    @Path("/gpu/{gpuId}")
    public Response findByGpu(
        @PathParam("gpuId") UUID gpuId,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(transactionService.findByGpu(gpuId, pagination)).build();
    }

    @GET
    @Path("/type/{type}")
    public Response findByType(
        @PathParam("type") String type,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(transactionService.findByTransactionType(type, pagination)).build();
    }

    @GET
    @Path("/date-range")
    public Response findByDateRange(
        @QueryParam("start") String start,
        @QueryParam("end") String end,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit")  @DefaultValue("10") int limit
    ) {
        offset = Math.max(0, offset);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        LocalDateTime startDate = start != null ? LocalDateTime.parse(start) : LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = end != null ? LocalDateTime.parse(end) : LocalDateTime.now();

        PaginationRequestDTO pagination = new PaginationRequestDTO(offset, limit);
        return Response.ok(transactionService.findByDateRange(startDate, endDate, pagination)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return transactionService.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response create(@Valid InventoryTransactionRequestDTO dto) {
        InventoryTransactionResponseDTO created = transactionService.createTransaction(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return transactionService.deleteTransaction(id) == 1
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
