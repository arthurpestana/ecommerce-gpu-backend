package org.acme.services.category;

import java.util.Optional;
import java.util.UUID;

import org.acme.dtos.category.CategoryRequestDTO;
import org.acme.dtos.category.CategoryResponseDTO;

import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface CategoryService {

    PaginationResponseDTO<CategoryResponseDTO> findCategoryByName(String name, PaginationRequestDTO pagination);
    Optional<CategoryResponseDTO> findCategoryById(UUID id);
    PaginationResponseDTO<CategoryResponseDTO> findAllCategories(PaginationRequestDTO pagination);
    CategoryResponseDTO createCategory(CategoryRequestDTO category);
    CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO category);
    Integer deleteCategory(UUID id);
}
