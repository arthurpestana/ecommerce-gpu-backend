package org.acme.services.model;

import java.util.Optional;
import java.util.UUID;

import org.acme.dtos.model.ModelRequestDTO;
import org.acme.dtos.model.ModelResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface ModelService {

    PaginationResponseDTO<ModelResponseDTO> findModelByName(String name, PaginationRequestDTO pagination);
    PaginationResponseDTO<ModelResponseDTO> findModelByManufacturer(UUID manufacturerId, PaginationRequestDTO pagination);
    Optional<ModelResponseDTO> findModelById(UUID id);
    PaginationResponseDTO<ModelResponseDTO> findAllModels(PaginationRequestDTO pagination);
    ModelResponseDTO createModel(ModelRequestDTO dto);
    ModelResponseDTO updateModel(UUID id, ModelRequestDTO dto);
    Integer deleteModel(UUID id);
}
