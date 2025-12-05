package org.acme.models.enums;

public enum PaymentMethod {
    CREDIT_CARD(1, "CREDIT_CARD"),
    DEBIT_CARD(2, "DEBIT_CARD"),
    PIX(3, "PIX"),
    BOLETO(4, "BOLETO");

    private final int id;
    private final String name;

    PaymentMethod(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PaymentMethod valueOf(Integer id) {
        if (id == null)
            return null;
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getId() == id)
                return method;
        }
        return null;
    }
}
