package org.acme.repositories;

import java.util.Optional;


import org.acme.models.Category;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepositoryBase<Category, String> {
    public PanacheQuery<Category> findAllCategories() {
        return findAll();
    }

    public Optional<Category> findCategoryById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Category> findByName(String name) {
        return find("Lower(name)", name.toLowerCase());
    }

    public boolean existsByName(String name) {
        return find("Lower(name)", name.toLowerCase()).firstResultOptional().isPresent();
    }

    public Long countAll() {
        return count();
    }

}
