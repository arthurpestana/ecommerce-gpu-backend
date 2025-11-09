package org.acme.services.gpu;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import org.acme.dtos.gpu.GpuRequestDTO;
import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.dtos.gpu.GpuStatusRequestDTO;
import org.acme.dtos.image.ImageUploadDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.dtos.technology.TechnologyRequestDTO;
import org.acme.models.Category;
import org.acme.models.Gpu;
import org.acme.models.Image;
import org.acme.models.Model;
import org.acme.models.Technology;
import org.acme.repositories.CategoryRepository;
import org.acme.repositories.GpuRepository;
import org.acme.repositories.ModelRepository;
import org.acme.repositories.TechnologyRepository;
import org.acme.services.image.ImageService;
import org.acme.services.storage.StorageService;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class GpuServiceImpl implements GpuService {

    @Inject
    GpuRepository gpuRepository;
    @Inject
    ModelRepository modelRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    TechnologyRepository technologyRepository;
    @Inject
    Validator validator;
    @Inject
    ImageService imageService;

    @Override
    public Optional<GpuResponseDTO> findGpuById(UUID id) {
        return gpuRepository.findGpuById(id).map(GpuResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findGpuByName(String name, PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByName(name).page(pagination.offset(), pagination.limit()).list();
        Long total = gpuRepository.findByName(name).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findAllGpus(PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findAllGpus().page(pagination.offset(), pagination.limit()).list();
        Long total = gpuRepository.findAllGpus().count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findByModel(UUID modelId, PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByModel(modelId).page(pagination.offset(), pagination.limit()).list();
        Long total = gpuRepository.findByModel(modelId).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findByManufacturer(UUID manufacturerId,
            PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByManufacturer(manufacturerId).page(pagination.offset(), pagination.limit())
                .list();
        Long total = gpuRepository.findByManufacturer(manufacturerId).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findByPriceRange(
            BigDecimal min,
            BigDecimal max,
            PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByPriceRange(min, max).page(pagination.offset(), pagination.limit()).list();

        Long total = gpuRepository.findByPriceRange(min, max).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findByTechnology(String techName, PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByTechnologyName(techName).page(pagination.offset(), pagination.limit())
                .list();

        Long total = gpuRepository.findByTechnologyName(techName).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findByCategory(String categoryName, PaginationRequestDTO pagination) {
        List<Gpu> gpus = gpuRepository.findByCategoryName(categoryName).page(pagination.offset(), pagination.limit())
                .list();

        Long total = gpuRepository.findByCategoryName(categoryName).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<GpuResponseDTO> findFiltered(
            String name,
            UUID modelId,
            UUID manufacturerId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isActive,
            PaginationRequestDTO pagination) {

        List<Gpu> gpus = gpuRepository.findFiltered(name, modelId, manufacturerId, minPrice, maxPrice, isActive)
                .page(pagination.offset(), pagination.limit()).list();

        Long total = gpuRepository.findFiltered(name, modelId, manufacturerId, minPrice, maxPrice, isActive).count();

        List<GpuResponseDTO> gpuList = gpus.stream()
                .map(GpuResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(gpuList, pagination.offset(), pagination.limit(), total);
    }

    @Override
    @Transactional
    public GpuResponseDTO createGpu(GpuRequestDTO dto, List<FileUpload> imagesUpload) {
        Model model = modelRepository.findByIdOptional(dto.modelId())
                .orElseThrow(() -> new NotFoundException("Modelo não encontrado"));

        Gpu gpu = new Gpu();
        applyGpu(dto, gpu, model);
        gpuRepository.persist(gpu);

        if (imagesUpload != null && !imagesUpload.isEmpty()) {
            imageService.uploadMultipleForGpu(gpu.getId(), imagesUpload);
        }
        return GpuResponseDTO.valueOf(gpu);
    }

    @Override
    @Transactional
    public GpuResponseDTO updateGpu(UUID id, GpuRequestDTO dto, List<FileUpload> imagesUpload) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Fabricante não pode ser nulo.");
        }

        Gpu gpu = gpuRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("GPU não encontrada"));

        Model model = modelRepository.findByIdOptional(dto.modelId())
                .orElseThrow(() -> new NotFoundException("Modelo não encontrado"));

        applyGpu(dto, gpu, model);
        gpuRepository.persist(gpu);

        if (imagesUpload != null && !imagesUpload.isEmpty()) {
            imageService.uploadMultipleForGpu(gpu.getId(), imagesUpload);
        }
        return GpuResponseDTO.valueOf(gpu);
    }

    @Override
    @Transactional
    public GpuResponseDTO setActiveStatus(UUID id, GpuStatusRequestDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID da GPU não pode ser nulo.");
        }

        Gpu gpu = gpuRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("GPU não encontrada com o ID: " + id));

        gpu.setIsActive(dto.isActive());
        gpuRepository.persist(gpu);
        return GpuResponseDTO.valueOf(gpu);
    }

    @Override
    @Transactional
    public Integer deleteGpu(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da GPU não pode ser nulo.");
        }

        Optional<Gpu> gpu = gpuRepository.findByIdOptional(id);
        if (gpu.isEmpty()) {
            throw new NotFoundException("GPU não encontrada com o ID: " + id);
        }

        return gpuRepository.deleteById(id) ? 1 : 0;
    }

    private void applyGpu(GpuRequestDTO dto, Gpu gpu, Model model) {
        ValidationUtils.validateDto(validator, dto);

        gpu.setName(StringUtils.safeTrim(dto.name()));
        gpu.setDescription(StringUtils.safeTrim(dto.description()));
        gpu.setPrice(dto.price());
        gpu.setIsActive(dto.isActive());
        gpu.setAvailableQuantity(dto.availableQuantity());
        gpu.setMemory(dto.memory());
        gpu.setArchitecture(StringUtils.safeTrim(dto.architecture()));
        gpu.setEnergyConsumption(dto.energyConsumption());
        gpu.setModel(model);

        if (dto.categoryIds() != null) {
            Set<Category> categories = dto.categoryIds().stream()
                    .map(id -> categoryRepository.findByIdOptional(id)
                            .orElseThrow(() -> new NotFoundException("Categoria não encontrada: " + id)))
                    .collect(Collectors.toSet());

            gpu.setCategories(categories);
        }

        if (dto.technologies() != null) {
            Set<Technology> technologies = new HashSet<>();

            for (TechnologyRequestDTO techDto : dto.technologies()) {
                Optional<Technology> technology = technologyRepository.findByName(techDto.name());
                Technology tech = null;

                if (technology.isEmpty()) {
                    Technology newTech = new Technology();
                    newTech.setName(StringUtils.safeTrim(techDto.name()));
                    newTech.setDescription(StringUtils.safeTrim(techDto.description()));
                    technologyRepository.persist(newTech);

                    tech = newTech;
                } else {
                    tech = technology.get();
                }

                technologies.add(tech);
            }

            gpu.setTechnologies(technologies);
        }
    }
}
