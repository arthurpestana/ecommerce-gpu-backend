package org.acme.services.category;

import java.util.Optional;


import org.acme.dtos.category.CategoryRequestDTO;
import org.acme.dtos.category.CategoryResponseDTO;

import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface CategoryService {

    PaginationResponseDTO<CategoryResponseDTO> findCategoryByName(String name, PaginationRequestDTO pagination);
    Optional<CategoryResponseDTO> findCategoryById(String id);
    PaginationResponseDTO<CategoryResponseDTO> findAllCategories(PaginationRequestDTO pagination);
    CategoryResponseDTO createCategory(CategoryRequestDTO category);
    CategoryResponseDTO updateCategory(String id, CategoryRequestDTO category);
    Integer deleteCategory(String id);
}
