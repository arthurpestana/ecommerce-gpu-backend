package org.acme.services.address;

import java.util.Optional;
import java.util.UUID;

import org.acme.dtos.address.AddressRequestDTO;
import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface AddressService {

    PaginationResponseDTO<AddressResponseDTO> findAddressByCity(String city, PaginationRequestDTO pagination);
    PaginationResponseDTO<AddressResponseDTO> findAddressByUser(UUID userId, PaginationRequestDTO pagination);
    Optional<AddressResponseDTO> findAddressById(UUID id);
    PaginationResponseDTO<AddressResponseDTO> findAllAddresses(PaginationRequestDTO pagination);
    AddressResponseDTO createAddress(AddressRequestDTO dto);
    AddressResponseDTO updateAddress(UUID id, AddressRequestDTO dto);
    Integer deleteAddress(UUID id);
}
