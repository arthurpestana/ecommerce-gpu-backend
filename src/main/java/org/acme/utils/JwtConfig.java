package org.acme.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.acme.models.User;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtConfig {
    private static final Duration EXPIRATION_TIME = Duration.ofHours(24);

    public String generateJwt(User user) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(EXPIRATION_TIME);

        Set<String> roles = new HashSet<String>();
        roles.add(user.getRole().toString());

        return Jwt.issuer("ecommerce_gpu")
            .subject(user.getId())
            .groups(roles)
            .expiresAt(expiryDate)
            .sign();

    }
}
