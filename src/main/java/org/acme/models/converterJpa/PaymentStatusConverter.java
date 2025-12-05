package org.acme.models.converterJpa;

import org.acme.models.enums.PaymentStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PaymentStatus paymentStatus) {
        return paymentStatus == null ? null : paymentStatus.getId();

    }

    @Override
    public PaymentStatus convertToEntityAttribute(Integer id) {
        return PaymentStatus.valueOf(id);
    }
    
}
