package org.acme.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "addresses")
public class Address extends DefaultEntity {
    @Column(name="street", length=150, nullable = false)
    private String street;

    @Column(name="city", length=100, nullable = false)
    private String city;

    @Column(name="state", length=100, nullable = false)
    private String state;

    @Column(name="zip_code", length=20, nullable = false)
    private String zipCode;

    @Column(name="country", length=100, nullable = false)
    private String country;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "address")
    private Set<Order> orders;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
