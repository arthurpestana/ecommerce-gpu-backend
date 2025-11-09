package org.acme.services.address;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.acme.dtos.address.AddressRequestDTO;
import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Address;
import org.acme.models.User;
import org.acme.repositories.AddressRepository;
import org.acme.repositories.UserRepository;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AddressServiceImpl implements AddressService {

    @Inject
    AddressRepository addressRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @Override
    public Optional<AddressResponseDTO> findAddressById(String id) {
        return addressRepository.findAddressById(id)
                .map(AddressResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<AddressResponseDTO> findAddressByCity(String city, PaginationRequestDTO pagination) {
        List<Address> addresses = addressRepository.findByCity(city)
                .page(pagination.offset(), pagination.limit())
                .list();

        Long total = addressRepository.findByCity(city).count();

        List<AddressResponseDTO> addressList = addresses.stream()
                .map(AddressResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(addressList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<AddressResponseDTO> findAddressByUser(String userId, PaginationRequestDTO pagination) {
        List<Address> addresses = addressRepository.findByUserId(userId)
                .page(pagination.offset(), pagination.limit())
                .list();

        Long total = addressRepository.findByUserId(userId).count();

        List<AddressResponseDTO> addressList = addresses.stream()
                .map(AddressResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(addressList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<AddressResponseDTO> findAllAddresses(PaginationRequestDTO pagination) {
        List<Address> addresses = addressRepository.findAllAddresses()
                .page(pagination.offset(), pagination.limit())
                .list();

        Long total = addressRepository.countAll();

        List<AddressResponseDTO> addressList = addresses.stream()
                .map(AddressResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(addressList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    @Transactional
    public AddressResponseDTO createAddress(AddressRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID informado."));

        Address address = new Address();
        address.setStreet(StringUtils.safeTrim(dto.street()));
        address.setCity(StringUtils.safeTrim(dto.city()));
        address.setState(StringUtils.safeTrim(dto.state()));
        address.setZipCode(StringUtils.safeTrim(dto.zipCode()));
        address.setCountry(StringUtils.safeTrim(dto.country()));
        address.setUser(user);

        addressRepository.persist(address);
        return AddressResponseDTO.valueOf(address);
    }

    @Override
    @Transactional
    public AddressResponseDTO updateAddress(String id, AddressRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        if (id == null) {
            throw new IllegalArgumentException("ID do Endereço não pode ser nulo.");
        }

        Address address = addressRepository.findAddressById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado com o ID: " + id));

        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID informado."));

        Address updatedAddress = address;
        updatedAddress.setStreet(StringUtils.safeTrim(dto.street()));
        updatedAddress.setCity(StringUtils.safeTrim(dto.city()));
        updatedAddress.setState(StringUtils.safeTrim(dto.state()));
        updatedAddress.setZipCode(StringUtils.safeTrim(dto.zipCode()));
        updatedAddress.setCountry(StringUtils.safeTrim(dto.country()));
        updatedAddress.setUser(user);

        addressRepository.persist(updatedAddress);
        return AddressResponseDTO.valueOf(updatedAddress);
    }

    @Override
    @Transactional
    public Integer deleteAddress(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Endereço não pode ser nulo.");
        }

        addressRepository.findAddressById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado com o ID: " + id));

        boolean deletedAddress = addressRepository.deleteById(id);
        return deletedAddress ? 1 : 0;
    }
}
