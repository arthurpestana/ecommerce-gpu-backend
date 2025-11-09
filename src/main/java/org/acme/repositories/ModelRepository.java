package org.acme.repositories;

import java.util.Optional;
import java.util.UUID;

import org.acme.models.Model;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelRepository implements PanacheRepositoryBase<Model, UUID> {

    public PanacheQuery<Model> findAllModels() {
        return findAll();
    }

    public Optional<Model> findModelById(UUID id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Model> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public PanacheQuery<Model> findByManufacturerId(UUID manufacturerId) {
        return find("manufacturer.id", manufacturerId);
    }

    public boolean existsByNameAndManufacturer(String name, UUID manufacturerId) {
        return find("LOWER(name) = ?1 AND manufacturer.id = ?2", name.toLowerCase(), manufacturerId)
                .firstResultOptional()
                .isPresent();
    }

    public Long countAll() {
        return count();
    }
}
