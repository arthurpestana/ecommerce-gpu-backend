package org.acme.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.acme.models.Gpu;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GpuRepository implements PanacheRepositoryBase<Gpu, String> {

    public PanacheQuery<Gpu> findAllGpus() {
        return find("ORDER BY createdAt DESC");
    }

    public Optional<Gpu> findGpuById(String id) {
        return find("id = ?1 ORDER BY createdAt DESC", id).firstResultOptional();
    }

    public PanacheQuery<Gpu> findByName(String name) {
        return find("LOWER(name) LIKE ?1 ORDER BY createdAt DESC", "%" + name.toLowerCase() + "%");
    }

    public boolean existsByName(String name) {
        return find("LOWER(name) = ?1", name.toLowerCase())
                .firstResultOptional()
                .isPresent();
    }

    public Long countAll() {
        return count();
    }

    public PanacheQuery<Gpu> findByModel(String modelId) {
        return find("model.id = ?1 ORDER BY createdAt DESC", modelId);
    }

    public PanacheQuery<Gpu> findByManufacturer(String manufacturerId) {
        return find("model.manufacturer.id = ?1 ORDER BY createdAt DESC", manufacturerId);
    }

    public PanacheQuery<Gpu> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {

        if (minPrice != null && maxPrice != null)
            return find("price BETWEEN ?1 AND ?2 ORDER BY createdAt DESC", minPrice, maxPrice);

        if (minPrice != null)
            return find("price >= ?1 ORDER BY createdAt DESC", minPrice);

        if (maxPrice != null)
            return find("price <= ?1 ORDER BY createdAt DESC", maxPrice);

        return find("ORDER BY createdAt DESC");
    }

    public PanacheQuery<Gpu> findByStockGreaterThan(Integer quantity) {
        return find("availableQuantity >= ?1 ORDER BY createdAt DESC", quantity);
    }

    public PanacheQuery<Gpu> findActive() {
        return find("isActive = true ORDER BY createdAt DESC");
    }

    public PanacheQuery<Gpu> findInactive() {
        return find("isActive = false ORDER BY createdAt DESC");
    }

    public PanacheQuery<Gpu> findByTechnologyName(String technologyName) {
        return find(
                "EXISTS (SELECT 1 FROM Gpu g JOIN g.technologies t WHERE g.id = id AND LOWER(t.name) = ?1) ORDER BY createdAt DESC",
                technologyName.toLowerCase());
    }

    public PanacheQuery<Gpu> findByCategoryName(String categoryName) {
        return find(
                "EXISTS (SELECT 1 FROM Gpu g JOIN g.categories c WHERE g.id = id AND LOWER(c.name) = ?1) ORDER BY createdAt DESC",
                categoryName.toLowerCase());
    }

    public PanacheQuery<Gpu> findFiltered(
            String name,
            String modelId,
            String manufacturerId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isActive,
            String categoryId) {

        StringBuilder query = new StringBuilder("SELECT DISTINCT g FROM Gpu g");
        var params = new java.util.ArrayList<>();

        if (categoryId != null) {
            query.append(" JOIN g.categories c");
        }

        query.append(" WHERE 1=1");

        if (name != null && !name.isBlank()) {
            query.append(" AND LOWER(g.name) LIKE ?").append(params.size() + 1);
            params.add("%" + name.toLowerCase() + "%");
        }

        if (modelId != null) {
            query.append(" AND g.model.id = ?").append(params.size() + 1);
            params.add(modelId);
        }

        if (manufacturerId != null) {
            query.append(" AND g.model.manufacturer.id = ?").append(params.size() + 1);
            params.add(manufacturerId);
        }

        if (minPrice != null) {
            query.append(" AND g.price >= ?").append(params.size() + 1);
            params.add(minPrice);
        }

        if (maxPrice != null) {
            query.append(" AND g.price <= ?").append(params.size() + 1);
            params.add(maxPrice);
        }

        if (isActive != null) {
            query.append(" AND g.isActive = ?").append(params.size() + 1);
            params.add(isActive);
        }

        if (categoryId != null) {
            query.append(" AND c.id = ?").append(params.size() + 1);
            params.add(categoryId);
        }

        query.append(" ORDER BY g.createdAt DESC");

        return find(query.toString(), params.toArray());
    }

    public PanacheQuery<Gpu> findByArchitecture(String architecture) {
        return find("LOWER(architecture) LIKE ?1 ORDER BY createdAt DESC",
                "%" + architecture.toLowerCase() + "%");
    }

    public PanacheQuery<Gpu> findByMaxEnergyConsumption(Integer maxWatts) {
        return find("energyConsumption <= ?1 ORDER BY createdAt DESC", maxWatts);
    }
}
