package org.acme.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "models")
public class Model extends DefaultEntity {

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="release_year", nullable = false)
    private Integer releaseYear;

    @ManyToOne()
    @JoinColumn(name="manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model")
    private List<Gpu> gpus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<Gpu> getGpus() {
        return gpus;
    }

    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }

}
