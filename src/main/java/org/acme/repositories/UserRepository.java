package org.acme.repositories;

import java.util.Optional;

import org.acme.models.User;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public PanacheQuery<User> findAllUsers() {
        return findAll();
    }

    public Optional<User> findUserById(Long id) {
        return findByIdOptional(id);
    }

    public Optional<User> findByEmail(String email) {
        return find("LOWER(email)", email.toLowerCase()).firstResultOptional();
    }

    public Optional<User> findByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return find("phoneNumber", phoneNumber).firstResultOptional();
    }

    public PanacheQuery<User> findByRole(String role) {
        return find("role", role);
    }

    public PanacheQuery<User> findByName(String name) {
        return find("LOWER(name) LIKE ?1", "%" + name.toLowerCase() + "%");
    }

    public PanacheQuery<User> findActiveUsers() {
        return find("isActive", true);
    }

    public boolean existsByEmail(String email) {
        return find("LOWER(email)", email.toLowerCase()).firstResultOptional().isPresent();
    }

    public boolean existsByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional().isPresent();
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return find("phoneNumber", phoneNumber).firstResultOptional().isPresent();
    }
    
    public long countAll() {
        return count();
    }

    public long countActiveUsers() {
        return count("isActive", true);
    }
}
