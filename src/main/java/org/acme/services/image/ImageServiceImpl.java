package org.acme.services.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.acme.dtos.image.ImageResponseDTO;
import org.acme.models.Gpu;
import org.acme.models.Image;
import org.acme.repositories.GpuRepository;
import org.acme.repositories.ImageRepository;
import org.acme.services.storage.StorageService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ImageServiceImpl implements ImageService {

    @Inject
    StorageService storageService;

    @Inject
    ImageRepository imageRepository;

    @Inject
    GpuRepository gpuRepository;

    @Override
    public void ensureBucket() {
        try {
            storageService.ensureBucketExists();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao verificar ou criar bucket", e);
        }
    }

    private String buildObjectName(String gpuId, String originalName) {
        String ext = "";

        int dotIdx = originalName.lastIndexOf('.');
        if (dotIdx > 0) {
            ext = originalName.substring(dotIdx);
        }

        return "gpus/%s/%s%s".formatted(
                gpuId,
                UUID.randomUUID(),
                ext);
    }

    @Override
    @Transactional
    public ImageResponseDTO uploadForGpu(String gpuId, FileUpload uploadFile, String altText) {
        Gpu gpu = gpuRepository.findByIdOptional(gpuId)
                .orElseThrow(() -> new NotFoundException("GPU não encontrada: " + gpuId));

        FileUpload file = uploadFile;
        Path tmpPath = file.uploadedFile();
        String originalName = file.fileName();
        String contentType = file.contentType();

        String objectName = buildObjectName(gpuId, originalName);

        try {
            byte[] bytes = Files.readAllBytes(tmpPath);

            String url = storageService.uploadBytes(objectName, bytes, contentType);

            Image img = new Image();
            img.setUrl(url);
            img.setAltText(altText != null ? altText : originalName);
            img.setGpu(gpu);
            img.setObjectName(objectName);

            imageRepository.persist(img);

            return ImageResponseDTO.valueOf(img);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar imagem " + originalName, e);
        }
    }

    @Override
    @Transactional
    public List<ImageResponseDTO> uploadMultipleForGpu(String gpuId, List<FileUpload> uploadFiles) {
        if (uploadFiles == null || uploadFiles.isEmpty()) {
            return List.of();
        }

        return uploadFiles.stream()
                .map(upload -> uploadForGpu(gpuId, upload, upload.fileName()))
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponseDTO findById(String id) {
        Image img = imageRepository.findImageById(id)
                .orElseThrow(() -> new NotFoundException("Imagem não encontrada: " + id));

        return ImageResponseDTO.valueOf(img);
    }

    @Override
    public List<ImageResponseDTO> findByGpu(String gpuId) {
        return imageRepository.findByGpu(gpuId).list()
                .stream()
                .map(ImageResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageResponseDTO> findAll() {
        return imageRepository.findAllImages().list()
                .stream()
                .map(ImageResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        Image img = imageRepository.findImageById(id)
                .orElseThrow(() -> new NotFoundException("Imagem não encontrada: " + id));

        try {
            storageService.deleteFile(img.getObjectName());
        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar arquivo do storage", e);
        }

        imageRepository.delete(img);
    }

    @Override
    @Transactional
    public void deleteManyFromGpu(String gpuId, List<String> imageIds) {

        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        List<Image> imgs = imageRepository.findByIdsInGpu(imageIds, gpuId).list();

        if (imgs.isEmpty()) {
            throw new NotFoundException("Nenhuma imagem encontrada para os IDs fornecidos.");
        }

        for (Image img : imgs) {
            try {
                storageService.deleteFile(img.getObjectName());
            } catch (Exception e) {
                throw new RuntimeException("Falha ao deletar arquivo " + img.getObjectName(), e);
            }

            imageRepository.delete(img);
        }
    }

    @Override
    @Transactional
    public void deleteByGpu(String gpuId) {
        List<Image> imgs = imageRepository.findByGpu(gpuId).list();

        for (Image img : imgs) {
            try {
                storageService.deleteFile(img.getObjectName());
            } catch (Exception e) {
                throw new RuntimeException("Falha ao deletar arquivo " + img.getObjectName(), e);
            }

            imageRepository.delete(img);
        }
    }
}
