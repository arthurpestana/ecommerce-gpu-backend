package org.acme.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.acme.models.Gpu;
import org.acme.models.Image;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImageRepository implements PanacheRepositoryBase<Image, UUID> {
    public PanacheQuery<Image> findAllImages() {
        return findAll();
    }

    public Optional<Image> findImageById(UUID id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Image> findByGpu(UUID gpuId) {
        return find("gpu.id = ?1", gpuId);
    }

    public PanacheQuery<Image> findByIdsInGpu(List<UUID> ids, UUID gpuId) {
        return find("id IN ?1 AND gpu.id = ?2", ids, gpuId);
    }
}
