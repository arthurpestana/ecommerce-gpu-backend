package org.acme.dtos.shared.pagination;

import java.util.*;

public record PaginationResponseDTO<T>(
    List<T> items,
    Integer page,
    Integer limit,
    Long total
) {
    public static <T> PaginationResponseDTO<T> valueOf(
        List<T> items,
        Integer page,
        Integer limit,
        Long total
    ) {
        return new PaginationResponseDTO<>(items, page, limit, total);
    }
}
