package org.acme.repositories;

import org.acme.models.Category;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {
    public PanacheQuery<Category> findAllCategories() {
        return findAll();
    }

    public PanacheQuery<Category> findByName(String name) {
        return find("Lower(name)", name.toLowerCase());
    }

    public boolean existsByName(String name) {
        return find("Lower(name)", name.toLowerCase()).firstResultOptional().isPresent();
    }

    public long countAll() {
        return count();
    }

}
