package org.acme.resources;

import org.acme.dtos.order.OrderRequestDTO;
import org.acme.dtos.order.OrderResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.services.order.OrderService;
import org.acme.utils.AuthenticatedUser;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    OrderService orderService;

    @Inject
    AuthenticatedUser auth;

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);
        PaginationResponseDTO<OrderResponseDTO> orders = orderService.findAllOrders(pagination);

        return Response.ok(orders).build();
    }

    @GET
    @Path("/user/{userId}")
    @RolesAllowed({ "ADMIN", "CUSTOMER" })
    public Response findByUser(
            @PathParam("userId") String userId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit) {
        if (auth.isCustomer() && !auth.getUserId().equals(userId)) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não tem permissão para acessar os endereços de outro usuário.")
                    .build();
        }

        page = Math.max(0, page);
        limit = Math.min(Math.max(1, limit), MAX_PAGE_SIZE);

        PaginationRequestDTO pagination = new PaginationRequestDTO(page, limit);

        return Response.ok(orderService.findByUser(userId, pagination)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "ADMIN", "CUSTOMER" })
    public Response findById(@PathParam("id") String id) {
        var orderOpt = orderService.findOrderById(id);

        if (orderOpt.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        OrderResponseDTO order = orderOpt.get();

        if (auth.isCustomer() && !auth.getUserId().equals(order.user().id())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Você não tem permissão para acessar o pedido de outro usuário.")
                    .build();
        }
        return Response.ok(order).build();
    }

    @POST
    @RolesAllowed({ "ADMIN", "CUSTOMER" })
    public Response create(@Valid OrderRequestDTO dto) {
        OrderResponseDTO response = orderService.createOrder(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

}
