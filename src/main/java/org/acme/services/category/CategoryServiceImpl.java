package org.acme.services.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;
import org.acme.dtos.category.CategoryRequestDTO;
import org.acme.dtos.category.CategoryResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Category;
import org.acme.repositories.CategoryRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.validation.Validator;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    Validator validator;

    @Override
    public Optional<CategoryResponseDTO> findCategoryById(String id) {
        return categoryRepository.findByIdOptional(id)
                .map(CategoryResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<CategoryResponseDTO> findCategoryByName(String name, PaginationRequestDTO pagination) {
        List<Category> categories = categoryRepository.findByName(name).page(pagination.page(), pagination.limit())
                .list();

        Long total = categoryRepository.findByName(name).count();

        List<CategoryResponseDTO> categoryList = categories.stream()
            .map(CategoryResponseDTO::valueOf)
            .collect(Collectors.toList());

        return new PaginationResponseDTO<>(
                categoryList,
                pagination.page(),
                pagination.limit(),
                total
        );
    }

    @Override
    public PaginationResponseDTO<CategoryResponseDTO> findAllCategories(PaginationRequestDTO pagination) {
        List<Category> categories = categoryRepository.findAllCategories()
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = categoryRepository.findAllCategories().count();

        List<CategoryResponseDTO> categoryList = categories.stream()
            .map(CategoryResponseDTO::valueOf)
            .collect(Collectors.toList());

        return new PaginationResponseDTO<>(
                categoryList,
                pagination.page(),
                pagination.limit(),
                total
        );
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        
        Category category = new Category();
        category.setName(StringUtils.safeTrim(dto.name()));
        category.setDescription(StringUtils.safeTrim(dto.description()));

        categoryRepository.persist(category);
        return CategoryResponseDTO.valueOf(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(String id, CategoryRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        if (id == null) {
            throw new IllegalArgumentException("ID da Categoria não pode ser nulo.");
        }

        Category category = categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com o ID: " + id));

        Category updatedCategory = category;
        updatedCategory.setName(StringUtils.safeTrim(dto.name()));
        updatedCategory.setDescription(StringUtils.safeTrim(dto.description()));

        categoryRepository.persist(updatedCategory);
        return CategoryResponseDTO.valueOf(updatedCategory);
    }

    @Override
    @Transactional
    public Integer deleteCategory(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da Categoria não pode ser nulo.");
        }

        categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com o ID: " + id));

        boolean deleted = categoryRepository.deleteById(id);
        return deleted ? 1 : 0;
    }
}
