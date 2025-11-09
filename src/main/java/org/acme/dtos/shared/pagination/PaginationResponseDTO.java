package org.acme.dtos.shared.pagination;

import java.util.*;

public record PaginationResponseDTO<T>(
    List<T> items,
    Integer offset,
    Integer limit,
    Long total
) {
    public static <T> PaginationResponseDTO<T> valueOf(
        List<T> items,
        Integer offset,
        Integer limit,
        Long total
    ) {
        return new PaginationResponseDTO<>(items, offset, limit, total);
    }
}
