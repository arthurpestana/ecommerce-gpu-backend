package org.acme.services.gpu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.gpu.GpuStatusRequestDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.jboss.resteasy.reactive.multipart.FileUpload;
public interface GpuService {

    PaginationResponseDTO<GpuResponseDTO> findGpuByName(String name, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findAllGpus(PaginationRequestDTO pagination);
    Optional<GpuResponseDTO> findGpuById(String id);

    PaginationResponseDTO<GpuResponseDTO> findByModel(String modelId, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByManufacturer(String manufacturerId, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByPriceRange(BigDecimal min, BigDecimal max, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByTechnology(String techName, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findByCategory(String categoryName, PaginationRequestDTO pagination);
    PaginationResponseDTO<GpuResponseDTO> findFiltered(String name, String modelId, String manufacturerId, BigDecimal minPrice, BigDecimal maxPrice, Boolean isActive, PaginationRequestDTO pagination);

    GpuResponseDTO createGpu(GpuRequestDTO dto, List<FileUpload> imagesUpload);
    GpuResponseDTO updateGpu(String id, GpuRequestDTO dto, List<FileUpload> imagesUpload);
    GpuResponseDTO setActiveStatus(String id, GpuStatusRequestDTO isActive);
    Integer deleteGpu(String id);
}
