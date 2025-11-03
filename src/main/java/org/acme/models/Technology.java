package org.acme.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "technologies")
public class Technology extends DefaultEntity {

    @Column(name="name", length=100, nullable = false)
    private String name;

    @Column(name="description", length=255, nullable = true)
    private String description;

    @ManyToMany(mappedBy = "technologies")
    private Set<Gpu> gpus;

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

    public Set<Gpu> getGpus() {
        return gpus;
    }

    public void setGpus(Set<Gpu> gpus) {
        this.gpus = gpus;
    }
    
}
