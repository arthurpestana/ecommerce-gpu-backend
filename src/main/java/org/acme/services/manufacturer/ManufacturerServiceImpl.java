package org.acme.services.manufacturer;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.acme.dtos.manufacturer.ManufacturerRequestDTO;
import org.acme.dtos.manufacturer.ManufacturerResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Manufacturer;
import org.acme.repositories.ManufacturerRepository;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ManufacturerServiceImpl implements ManufacturerService {

    @Inject
    ManufacturerRepository manufacturerRepository;

    @Inject
    Validator validator;

    @Override
    public Optional<ManufacturerResponseDTO> findManufacturerById(String id) {
        return manufacturerRepository.findManufacturerById(id)
                .map(ManufacturerResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<ManufacturerResponseDTO> findManufacturerByName(String name, PaginationRequestDTO pagination) {
        List<Manufacturer> manufacturers = manufacturerRepository.findByName(name)
                .page(pagination.offset(), pagination.limit())
                .list();

        Long total = manufacturerRepository.findByName(name).count();

        List<ManufacturerResponseDTO> list = manufacturers.stream()
                .map(ManufacturerResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<ManufacturerResponseDTO> findAllManufacturers(PaginationRequestDTO pagination) {
        List<Manufacturer> manufacturers = manufacturerRepository.findAllManufacturers()
                .page(pagination.offset(), pagination.limit())
                .list();

        Long total = manufacturerRepository.countAll();

        List<ManufacturerResponseDTO> list = manufacturers.stream()
                .map(ManufacturerResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.offset(), pagination.limit(), total);
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        if (manufacturerRepository.existsByCpnj(dto.cpnj())) {
            throw new IllegalArgumentException("CNPJ já está cadastrado.");
        }

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(StringUtils.safeTrim(dto.name()));
        manufacturer.setEmail(StringUtils.safeTrim(dto.email()));
        manufacturer.setCpnj(StringUtils.normalizeDigits(dto.cpnj()));
        manufacturer.setCountry(StringUtils.safeTrim(dto.country()));

        manufacturerRepository.persist(manufacturer);
        return ManufacturerResponseDTO.valueOf(manufacturer);
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO updateManufacturer(String id, ManufacturerRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        if (id == null) {
            throw new IllegalArgumentException("ID do Fabricante não pode ser nulo.");
        }

        Manufacturer manufacturer = manufacturerRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Fabricante não encontrado com o ID: " + id));

        ValidationUtils.validateDto(validator, dto);
        Manufacturer updatedManufacturer = manufacturer;

        updatedManufacturer.setName(StringUtils.safeTrim(dto.name()));
        updatedManufacturer.setEmail(StringUtils.safeTrim(dto.email()));
        updatedManufacturer.setCpnj(StringUtils.normalizeDigits(dto.cpnj()));
        updatedManufacturer.setCountry(StringUtils.safeTrim(dto.country()));

        manufacturerRepository.persist(updatedManufacturer);
        return ManufacturerResponseDTO.valueOf(updatedManufacturer);
    }

    @Override
    @Transactional
    public Integer deleteManufacturer(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Fabricante não pode ser nulo.");
        }

        manufacturerRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Fabricante não encontrado com o ID: " + id));

        boolean deletedManufacturer = manufacturerRepository.deleteById(id);
        return deletedManufacturer ? 1 : 0;
    }
}
