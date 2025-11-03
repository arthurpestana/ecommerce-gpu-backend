package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Model;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelRepository implements PanacheRepository<Model> {

    public PanacheQuery<Model> findAllModels() {
        return findAll();
    }

    public Optional<Model> findModelById(Long id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Model> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public PanacheQuery<Model> findByManufacturerId(Long manufacturerId) {
        return find("manufacturer.id", manufacturerId);
    }

    public boolean existsByNameAndManufacturer(String name, Long manufacturerId) {
        return find("LOWER(name) = ?1 AND manufacturer.id = ?2", name.toLowerCase(), manufacturerId)
                .firstResultOptional()
                .isPresent();
    }

    public long countAll() {
        return count();
    }
}
