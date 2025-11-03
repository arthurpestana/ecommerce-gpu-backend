package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Address;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {

    public PanacheQuery<Address> findAllAddresses() {
        return findAll();
    }

    public Optional<Address> findAddressById(Long id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Address> findByCity(String city) {
        return find("LOWER(city) LIKE ?1", "%" + city.toLowerCase() + "%");
    }

    public PanacheQuery<Address> findByUserId(Long userId) {
        return find("user.id", userId);
    }

    public long countAll() {
        return count();
    }
}
