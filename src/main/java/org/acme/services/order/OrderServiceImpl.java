package org.acme.services.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.acme.dtos.inventory.InventoryTransactionRequestDTO;
import org.acme.dtos.order.OrderItemRequestDTO;
import org.acme.dtos.order.OrderRequestDTO;
import org.acme.dtos.order.OrderResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Gpu;
import org.acme.models.Order;
import org.acme.models.OrderItem;
import org.acme.models.Payment;
import org.acme.models.enums.OrderStatus;
import org.acme.models.enums.PaymentStatus;
import org.acme.models.enums.TransactionTypes;
import org.acme.repositories.AddressRepository;
import org.acme.repositories.GpuRepository;
import org.acme.repositories.OrderRepository;
import org.acme.repositories.PaymentRepository;
import org.acme.repositories.UserRepository;
import org.acme.services.inventory.InventoryTransactionService;
import org.acme.services.paymentGateway.PaymentGatewayService;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    OrderRepository orderRepository;

    // @Inject
    // OrderItemRepository orderItemRepository;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    GpuRepository gpuRepository;

    @Inject
    AddressRepository addressRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PaymentGatewayService paymentGatewayService;

    @Inject
    InventoryTransactionService inventoryService;

    @Inject
    Validator validator;

    @Override
    public Optional<OrderResponseDTO> findOrderById(String id) {
        return orderRepository.findOrderById(id)
                .map(OrderResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<OrderResponseDTO> findAllOrders(PaginationRequestDTO pagination) {
        List<Order> orders = orderRepository.findAllOrders()
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = orderRepository.findAllOrders().count();

        List<OrderResponseDTO> list = orders.stream()
                .map(OrderResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<OrderResponseDTO> findByUser(String userId, PaginationRequestDTO pagination) {
        List<Order> orders = orderRepository.findByUserId(userId)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = orderRepository.findByUserId(userId).count();

        List<OrderResponseDTO> list = orders.stream()
                .map(OrderResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        ValidationUtils.validateDto(validator, dto);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        var user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        var address = addressRepository.findByIdOptional(dto.addressId())
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        order.setAddress(address);

        order.setUser(user);

        applyOrderItems(dto.items(), order);

        orderRepository.persist(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.persist(payment);

        var checkout = paymentGatewayService.createCheckoutPayment(payment);
        payment.setGatewayPaymentId(checkout.gatewayPaymentId());

        System.out.println("Checkout URL: " + checkout.checkoutUrl());
        return OrderResponseDTO.valueOf(order, checkout.checkoutUrl());
        // return OrderResponseDTO.valueOf(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, String paymentStatus) {

        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

        paymentStatus = paymentStatus.toUpperCase();

        switch (paymentStatus) {
            case "APPROVED" -> {
                order.setOrderStatus(OrderStatus.PAID);

                for (OrderItem item : order.getItems()) {
                    inventoryService.createTransaction(
                            new InventoryTransactionRequestDTO(
                                    item.getGpu().getId(),
                                    item.getQuantity(),
                                    TransactionTypes.REMOVE,
                                    "Pedido aprovado - baixa de estoque",
                                    LocalDateTime.now())

                    );
                }
            }
            case "REJECTED" -> order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            case "CANCELED" -> order.setOrderStatus(OrderStatus.CANCELED);
            default -> {
            }
        }

        orderRepository.persist(order);
    }

    private void applyOrderItems(List<OrderItemRequestDTO> items, Order order) {
        for (OrderItemRequestDTO dto : items) {

            Gpu gpu = gpuRepository.findByIdOptional(dto.gpuId())
                    .orElseThrow(() -> new NotFoundException("GPU não encontrada"));

            if (gpu.getAvailableQuantity() < dto.quantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para GPU: " + gpu.getName());
            }

            OrderItem item = new OrderItem();
            item.setGpu(gpu);
            item.setQuantity(dto.quantity());
            item.setPrice(gpu.getPrice());
            item.setOrder(order);

            order.getItems().add(item);
        }

        order.recalculateTotal();
    }
}
