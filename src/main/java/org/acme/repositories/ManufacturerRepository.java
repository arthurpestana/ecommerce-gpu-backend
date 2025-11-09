package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Manufacturer;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManufacturerRepository implements PanacheRepositoryBase<Manufacturer, String> {

    public PanacheQuery<Manufacturer> findAllManufacturers() {
        return findAll();
    }

    public Optional<Manufacturer> findManufacturerById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Manufacturer> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public Optional<Manufacturer> findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResultOptional();
    }

    public boolean existsByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResultOptional().isPresent();
    }

    public Long countAll() {
        return count();
    }
}
