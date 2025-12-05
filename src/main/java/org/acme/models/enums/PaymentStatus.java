package org.acme.models.enums;

public enum PaymentStatus {
    PENDING(1, "PENDING"),
    APPROVED(2, "APPROVED"),
    REJECTED(3, "REJECTED"),
    CANCELED(4, "CANCELED");

    private final int id;
    private final String name;

    PaymentStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PaymentStatus valueOf(Integer id) {
        if (id == null)
            return null;
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getId() == id)
                return status;
        }
        return null;
    }
}
