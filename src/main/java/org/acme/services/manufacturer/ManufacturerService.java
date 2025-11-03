package org.acme.services.manufacturer;

import java.util.Optional;

import org.acme.dtos.manufacturer.ManufacturerRequestDTO;
import org.acme.dtos.manufacturer.ManufacturerResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface ManufacturerService {

    PaginationResponseDTO<ManufacturerResponseDTO> findManufacturerByName(String name, PaginationRequestDTO pagination);
    Optional<ManufacturerResponseDTO> findManufacturerById(Long id);
    PaginationResponseDTO<ManufacturerResponseDTO> findAllManufacturers(PaginationRequestDTO pagination);
    ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO dto);
    ManufacturerResponseDTO updateManufacturer(Long id, ManufacturerRequestDTO dto);
    Integer deleteManufacturer(Long id);
}
