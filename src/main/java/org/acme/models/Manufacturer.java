package org.acme.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends Person {

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "manufacturer", cascade=CascadeType.ALL)
    private List<Model> models;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
