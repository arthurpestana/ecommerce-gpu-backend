package org.acme.models.converterJpa;

import org.acme.models.enums.TransactionTypes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypesConverter implements AttributeConverter<TransactionTypes, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransactionTypes transactionTypes) {
        return transactionTypes == null ? null : transactionTypes.getId();

    }

    @Override
    public TransactionTypes convertToEntityAttribute(Integer id) {
        return TransactionTypes.valueOf(id);
    }
    
}
