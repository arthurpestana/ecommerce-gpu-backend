package org.acme.models;

import java.math.BigDecimal;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "gpus")
public class Gpu extends DefaultEntity {

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "memory", nullable = false)
    private Integer memory;

    @Column(name = "architecture", length = 255, nullable = false)
    private String architecture;

    @Column(name = "energy_consumption", nullable = true)
    private Integer energyConsumption;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @OneToMany(mappedBy = "gpu", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "gpu", cascade = CascadeType.ALL)
    private List<InventoryTransaction> inventoryTransactions;

    @ManyToMany
    @JoinTable(name = "gpu_technologies", joinColumns = @JoinColumn(name = "gpu_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private Set<Technology> technologies;

    @ManyToMany
    @JoinTable(name = "gpu_categories", joinColumns = @JoinColumn(name = "gpu_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public Integer getEnergyConsumption() {
        return energyConsumption;
    }
    
    public void setEnergyConsumption(Integer energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<InventoryTransaction> getInventoryTransactions() {
        return inventoryTransactions;
    }

    public void setInventoryTransactions(List<InventoryTransaction> inventoryTransactions) {
        this.inventoryTransactions = inventoryTransactions;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

}
