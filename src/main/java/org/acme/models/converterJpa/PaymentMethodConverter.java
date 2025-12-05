package org.acme.models.converterJpa;

import org.acme.models.enums.PaymentMethod;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PaymentMethod paymentMethod) {
        return paymentMethod == null ? null : paymentMethod.getId();

    }

    @Override
    public PaymentMethod convertToEntityAttribute(Integer id) {
        return PaymentMethod.valueOf(id);
    }
    
}
