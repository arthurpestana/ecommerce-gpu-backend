package org.acme.services.image;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.acme.dtos.image.ImageResponseDTO;
import org.acme.dtos.image.ImageUploadDTO;

public interface ImageService {

    void ensureBucket();

    ImageResponseDTO uploadForGpu(
            UUID gpuId,
            FileUpload uploadFile,
            String altText);

    List<ImageResponseDTO> uploadMultipleForGpu(
            UUID gpuId,
            List<FileUpload> uploadFiles);
    ImageResponseDTO findById(UUID id);

    List<ImageResponseDTO> findByGpu(UUID gpuId);

    List<ImageResponseDTO> findAll();

    void delete(UUID id);

    void deleteManyFromGpu(UUID gpuId, List<UUID> imageIds);

    void deleteByGpu(UUID gpuId);
}
