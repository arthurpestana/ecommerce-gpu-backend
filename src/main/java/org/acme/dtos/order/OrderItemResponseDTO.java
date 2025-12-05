package org.acme.dtos.order;

import java.math.BigDecimal;

import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.models.OrderItem;

public record OrderItemResponseDTO(
    String id,
    Integer quantity,
    BigDecimal price,
    GpuResponseDTO gpu
) {
    public static OrderItemResponseDTO valueOf(OrderItem item) {
        if (item == null) return null;

        return new OrderItemResponseDTO(
            item.getId(),
            item.getQuantity(),
            item.getPrice(),
            GpuResponseDTO.valueOf(item.getGpu())
        );
    }
}
