package org.acme.models.converterJpa;

import org.acme.models.enums.UserRoles;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRolesConverter implements AttributeConverter<UserRoles, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRoles userRoles) {
        return userRoles == null ? null : userRoles.getId();

    }

    @Override
    public UserRoles convertToEntityAttribute(Integer id) {
        return UserRoles.valueOf(id);
    }
    
}
