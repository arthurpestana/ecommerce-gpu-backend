package org.acme.models.enums;

public enum OrderStatus {
    AWAITING_PAYMENT(1, "AWAITING_PAYMENT"),
    PROCESSING(2, "PROCESSING"),
    SHIPPED(3, "SHIPPED"),
    DELIVERED(4, "DELIVERED"),
    CANCELED(5, "CANCELED");

    private final int id;
    private final String name;

    OrderStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static OrderStatus valueOf(Integer id) {
        if (id == null)
            return null;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getId() == id)
                return status;
        }
        return null;
    }
}
