package org.acme.repositories;

import java.util.List;
import java.util.Optional;


import org.acme.models.Gpu;
import org.acme.models.Image;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImageRepository implements PanacheRepositoryBase<Image, String> {
    public PanacheQuery<Image> findAllImages() {
        return findAll();
    }

    public Optional<Image> findImageById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Image> findByGpu(String gpuId) {
        return find("gpu.id = ?1", gpuId);
    }

    public PanacheQuery<Image> findByIdsInGpu(List<String> ids, String gpuId) {
        return find("id IN ?1 AND gpu.id = ?2", ids, gpuId);
    }
}
