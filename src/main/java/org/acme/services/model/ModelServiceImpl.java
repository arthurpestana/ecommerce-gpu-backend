package org.acme.services.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.acme.dtos.model.ModelRequestDTO;
import org.acme.dtos.model.ModelResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Manufacturer;
import org.acme.models.Model;
import org.acme.repositories.ManufacturerRepository;
import org.acme.repositories.ModelRepository;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ModelServiceImpl implements ModelService {

    @Inject
    ModelRepository modelRepository;

    @Inject
    ManufacturerRepository manufacturerRepository;

    @Inject
    Validator validator;

    @Override
    public Optional<ModelResponseDTO> findModelById(String id) {
        return modelRepository.findModelById(id)
                .map(ModelResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<ModelResponseDTO> findModelByName(String name, PaginationRequestDTO pagination) {
        List<Model> models = modelRepository.findByName(name)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = modelRepository.findByName(name).count();

        List<ModelResponseDTO> list = models.stream()
                .map(ModelResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<ModelResponseDTO> findModelByManufacturer(String manufacturerId, PaginationRequestDTO pagination) {
        List<Model> models = modelRepository.findByManufacturerId(manufacturerId)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = modelRepository.findByManufacturerId(manufacturerId).count();

        List<ModelResponseDTO> list = models.stream()
                .map(ModelResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<ModelResponseDTO> findAllModels(PaginationRequestDTO pagination) {
        List<Model> models = modelRepository.findAllModels()
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = modelRepository.countAll();

        List<ModelResponseDTO> list = models.stream()
                .map(ModelResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    @Transactional
    public ModelResponseDTO createModel(ModelRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(dto.manufacturerId())
                .orElseThrow(() -> new NotFoundException("Fabricante não encontrado para o ID informado."));

        if (modelRepository.existsByNameAndManufacturer(dto.name(), dto.manufacturerId())) {
            throw new IllegalArgumentException("Já existe um modelo com esse nome para este fabricante.");
        }

        Model model = new Model();
        model.setName(StringUtils.safeTrim(dto.name()));
        model.setReleaseYear(dto.releaseYear());
        model.setManufacturer(manufacturer);

        modelRepository.persist(model);
        return ModelResponseDTO.valueOf(model);
    }

    @Override
    @Transactional
    public ModelResponseDTO updateModel(String id, ModelRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        if (id == null) {
            throw new IllegalArgumentException("ID do Modelo não pode ser nulo.");
        }

        Model model = modelRepository.findModelById(id)
                .orElseThrow(() -> new NotFoundException("Modelo não encontrado com o ID: " + id));

        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(dto.manufacturerId())
                .orElseThrow(() -> new NotFoundException("Fabricante não encontrado para o ID informado."));

        Model updatedModel = model;
        updatedModel.setName(StringUtils.safeTrim(dto.name()));
        updatedModel.setReleaseYear(dto.releaseYear());
        updatedModel.setManufacturer(manufacturer);

        modelRepository.persist(updatedModel);
        return ModelResponseDTO.valueOf(updatedModel);
    }

    @Override
    @Transactional
    public Integer deleteModel(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Modelo não pode ser nulo.");
        }
        
        modelRepository.findModelById(id)
                .orElseThrow(() -> new NotFoundException("Modelo não encontrado com o ID: " + id));

        boolean deleted = modelRepository.deleteById(id);
        return deleted ? 1 : 0;
    }
}
