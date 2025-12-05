package org.acme.utils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class AuthenticatedUser {

    @Inject
    JsonWebToken jwt;

    public String getUserId() {
        return jwt.getSubject();
    }

    public String getRole() {
        return jwt.getGroups().stream().findFirst().orElse("CUSTOMER");
    }

    public boolean isAdmin() {
        return jwt.getGroups().contains("ADMIN");
    }

    public boolean isCustomer() {
        return jwt.getGroups().contains("CUSTOMER");
    }
}
