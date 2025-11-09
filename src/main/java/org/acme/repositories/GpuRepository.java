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
        return findAll();
    }

    public Optional<Gpu> findGpuById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Gpu> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public boolean existsByName(String name) {
        return find("LOWER(name) = ?1", name.toLowerCase()).firstResultOptional().isPresent();
    }

    public Long countAll() {
        return count();
    }

    public PanacheQuery<Gpu> findByModel(String modelId) {
        return find("model.id = ?1", modelId);
    }

    public PanacheQuery<Gpu> findByManufacturer(String manufacturerId) {
        return find("model.manufacturer.id = ?1", manufacturerId);
    }

    public PanacheQuery<Gpu> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null)
            return find("price BETWEEN ?1 AND ?2", minPrice, maxPrice);
        if (minPrice != null)
            return find("price >= ?1", minPrice);
        if (maxPrice != null)
            return find("price <= ?1", maxPrice);
        return findAll();
    }

    public PanacheQuery<Gpu> findByStockGreaterThan(Integer quantity) {
        return find("availableQuantity >= ?1", quantity);
    }

    public PanacheQuery<Gpu> findActive() {
        return find("isActive = true");
    }

    public PanacheQuery<Gpu> findInactive() {
        return find("isActive = false");
    }

    public PanacheQuery<Gpu> findByTechnologyName(String technologyName) {
        return find("EXISTS (SELECT 1 FROM g.technologies t WHERE LOWER(t.name) = ?1)", technologyName.toLowerCase());
    }

    public PanacheQuery<Gpu> findByCategoryName(String categoryName) {
        return find("EXISTS (SELECT 1 FROM g.categories c WHERE LOWER(c.name) = ?1)", categoryName.toLowerCase());
    }

    public PanacheQuery<Gpu> findFiltered(
        String name,
        String modelId,
        String manufacturerId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean isActive
    ) {
        StringBuilder query = new StringBuilder("1=1");
        var params = new java.util.ArrayList<>();

        if (name != null && !name.isBlank()) {
            query.append(" AND LOWER(name) LIKE ?").append(params.size() + 1);
            params.add("%" + name.toLowerCase() + "%");
        }
        if (modelId != null) {
            query.append(" AND model.id = ?").append(params.size() + 1);
            params.add(modelId);
        }
        if (manufacturerId != null) {
            query.append(" AND model.manufacturer.id = ?").append(params.size() + 1);
            params.add(manufacturerId);
        }
        if (minPrice != null) {
            query.append(" AND price >= ?").append(params.size() + 1);
            params.add(minPrice);
        }
        if (maxPrice != null) {
            query.append(" AND price <= ?").append(params.size() + 1);
            params.add(maxPrice);
        }
        if (isActive != null) {
            query.append(" AND isActive = ?").append(params.size() + 1);
            params.add(isActive);
        }

        return find(query.toString(), params.toArray());
    }

    public PanacheQuery<Gpu> findByArchitecture(String architecture) {
        return find("LOWER(architecture) LIKE ?1", "%" + architecture.toLowerCase() + "%");
    }

    public PanacheQuery<Gpu> findByMaxEnergyConsumption(Integer maxWatts) {
        return find("energyConsumption <= ?1", maxWatts);
    }
}
