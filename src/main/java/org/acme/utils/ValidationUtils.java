package org.acme.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static <T> void validateDto(Validator validator, T dto) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
