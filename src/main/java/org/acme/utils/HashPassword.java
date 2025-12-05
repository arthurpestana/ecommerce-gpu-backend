package org.acme.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HashPassword {
    private String salt = "#blahxyz17";
    private Integer iterationCount = 405;
    private Integer keyLength = 512;

    public String getHashPassword(String password) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(
                            new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isHash(String password) {
        if (password == null || password.length() < 80) {
            return false;
        }

        return password.matches("^[A-Za-z0-9+/=]+$");
    }
}
