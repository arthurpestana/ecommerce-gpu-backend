package org.acme.repositories;

import java.util.Optional;
import java.util.UUID;

import org.acme.models.Address;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepositoryBase<Address, UUID> {

    public PanacheQuery<Address> findAllAddresses() {
        return findAll();
    }

    public Optional<Address> findAddressById(UUID id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Address> findByCity(String city) {
        return find("LOWER(city) LIKE ?1", "%" + city.toLowerCase() + "%");
    }

    public PanacheQuery<Address> findByUserId(UUID userId) {
        return find("user.id", userId);
    }

    public Long countAll() {
        return count();
    }
}
