package org.acme.services.image;

import java.util.List;


import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.acme.dtos.image.ImageResponseDTO;
import org.acme.dtos.image.ImageUploadDTO;

public interface ImageService {

    void ensureBucket();

    ImageResponseDTO uploadForGpu(
            String gpuId,
            FileUpload uploadFile,
            String altText);

    List<ImageResponseDTO> uploadMultipleForGpu(
            String gpuId,
            List<FileUpload> uploadFiles);
    ImageResponseDTO findById(String id);

    List<ImageResponseDTO> findByGpu(String gpuId);

    List<ImageResponseDTO> findAll();

    void delete(String id);

    void deleteManyFromGpu(String gpuId, List<String> imageIds);

    void deleteByGpu(String gpuId);
}
