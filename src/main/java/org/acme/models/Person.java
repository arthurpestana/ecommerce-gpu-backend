package org.acme.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Person extends DefaultEntity {
    
    @Column(name="name", nullable=false)
    private String name;

    @Column(name="email", nullable=false, unique=true)
    private String email;

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
