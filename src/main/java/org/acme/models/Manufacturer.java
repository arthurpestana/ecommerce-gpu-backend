package org.acme.models;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends Person {

    @Column(name = "cpnj", nullable = false, unique = true)
    private String cpnj;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "manufacturer", cascade=CascadeType.ALL)
    private List<Model> models;

    public String getCpnj() {
        return cpnj;
    }

    public void setCpnj(String cpnj) {
        this.cpnj = cpnj;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
