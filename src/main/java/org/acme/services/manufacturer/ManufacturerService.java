package org.acme.services.manufacturer;

import java.util.Optional;
import java.util.UUID;

import org.acme.dtos.manufacturer.ManufacturerRequestDTO;
import org.acme.dtos.manufacturer.ManufacturerResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface ManufacturerService {

    PaginationResponseDTO<ManufacturerResponseDTO> findManufacturerByName(String name, PaginationRequestDTO pagination);
    Optional<ManufacturerResponseDTO> findManufacturerById(UUID id);
    PaginationResponseDTO<ManufacturerResponseDTO> findAllManufacturers(PaginationRequestDTO pagination);
    ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO dto);
    ManufacturerResponseDTO updateManufacturer(UUID id, ManufacturerRequestDTO dto);
    Integer deleteManufacturer(UUID id);
}
