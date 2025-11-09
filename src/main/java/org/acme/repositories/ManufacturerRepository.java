package org.acme.repositories;

import java.util.Optional;
import java.util.UUID;

import org.acme.models.Manufacturer;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManufacturerRepository implements PanacheRepositoryBase<Manufacturer, UUID> {

    public PanacheQuery<Manufacturer> findAllManufacturers() {
        return findAll();
    }

    public Optional<Manufacturer> findManufacturerById(UUID id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Manufacturer> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public Optional<Manufacturer> findByCpnj(String cpnj) {
        return find("cpnj", cpnj).firstResultOptional();
    }

    public boolean existsByCpnj(String cpnj) {
        return find("cpnj", cpnj).firstResultOptional().isPresent();
    }

    public Long countAll() {
        return count();
    }
}
