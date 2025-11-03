package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Manufacturer;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManufacturerRepository implements PanacheRepository<Manufacturer> {

    /** Retorna todos os fabricantes */
    public PanacheQuery<Manufacturer> findAllManufacturers() {
        return findAll();
    }

    /** Busca por ID */
    public Optional<Manufacturer> findManufacturerById(Long id) {
        return findByIdOptional(id);
    }

    /** Busca por nome (case-insensitive) */
    public PanacheQuery<Manufacturer> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    /** Busca por CNPJ */
    public Optional<Manufacturer> findByCpnj(String cpnj) {
        return find("cpnj", cpnj).firstResultOptional();
    }

    /** Verifica duplicidade de CNPJ */
    public boolean existsByCpnj(String cpnj) {
        return find("cpnj", cpnj).firstResultOptional().isPresent();
    }

    /** Conta total de fabricantes */
    public long countAll() {
        return count();
    }
}
