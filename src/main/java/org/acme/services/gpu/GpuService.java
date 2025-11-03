package org.acme.services.gpu;

import java.math.BigDecimal;
import java.util.Optional;

import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface GpuService {

    PaginationResponseDTO<GpuResponseDTO> findGpuByName(String name, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findAllGpus(PaginationRequestDTO pagination);
    Optional<GpuResponseDTO> findGpuById(Long id);

    PaginationResponseDTO<GpuResponseDTO> findByModel(Long modelId, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByManufacturer(Long manufacturerId, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByPriceRange(BigDecimal min, BigDecimal max, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByTechnology(String techName, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByCategory(String categoryName, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findFiltered(String name, Long modelId, Long manufacturerId, BigDecimal minPrice, BigDecimal maxPrice, Boolean isActive, PaginationRequestDTO pagination);

    GpuResponseDTO createGpu(GpuRequestDTO dto);
    GpuResponseDTO updateGpu(Long id, GpuRequestDTO dto);
    Integer deleteGpu(Long id);
}
