package org.acme.dtos.gpu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.acme.dtos.category.CategoryResponseDTO;
import org.acme.dtos.image.ImageResponseDTO;
import org.acme.dtos.model.ModelResponseDTO;
import org.acme.dtos.technology.TechnologyResponseDTO;
import org.acme.models.Gpu;

public record GpuResponseDTO(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    Boolean isActive,
    Integer availableQuantity,
    Integer memory,
    String architecture,
    Integer energyConsumption,
    ModelResponseDTO model,
    List<ImageResponseDTO> images,
    Set<TechnologyResponseDTO> technologies,
    Set<CategoryResponseDTO> categories
) {
    public static GpuResponseDTO valueOf(Gpu gpu) {
        if (gpu == null) return null;

        return new GpuResponseDTO(
            gpu.getId(),
            gpu.getName(),
            gpu.getDescription(),
            gpu.getPrice(),
            gpu.getIsActive(),
            gpu.getAvailableQuantity(),
            gpu.getMemory(),
            gpu.getArchitecture(),
            gpu.getEnergyConsumption(),
            ModelResponseDTO.valueOf(gpu.getModel()),
            gpu.getImages() != null ? gpu.getImages().stream().map(ImageResponseDTO::valueOf).toList() : List.of(),
            gpu.getTechnologies() != null ? gpu.getTechnologies().stream().map(TechnologyResponseDTO::valueOf).collect(java.util.stream.Collectors.toSet()) : Set.of(),
            gpu.getCategories() != null ? gpu.getCategories().stream().map(CategoryResponseDTO::valueOf).collect(java.util.stream.Collectors.toSet()) : Set.of()
        );
    }
}
