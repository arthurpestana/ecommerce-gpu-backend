package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Technology;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TechnologyRepository implements PanacheRepository<Technology> {
    public Optional<Technology> findByName(String name) {
        return find("LOWER(name) = ?1", name.toLowerCase()).firstResultOptional();
    }
}
