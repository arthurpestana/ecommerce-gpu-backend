package org.acme.services.address;

import java.util.Optional;

import org.acme.dtos.address.AddressRequestDTO;
import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface AddressService {

    PaginationResponseDTO<AddressResponseDTO> findAddressByCity(String city, PaginationRequestDTO pagination);
    PaginationResponseDTO<AddressResponseDTO> findAddressByUser(Long userId, PaginationRequestDTO pagination);
    Optional<AddressResponseDTO> findAddressById(Long id);
    PaginationResponseDTO<AddressResponseDTO> findAllAddresses(PaginationRequestDTO pagination);
    AddressResponseDTO createAddress(AddressRequestDTO dto);
    AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto);
    Integer deleteAddress(Long id);
}
