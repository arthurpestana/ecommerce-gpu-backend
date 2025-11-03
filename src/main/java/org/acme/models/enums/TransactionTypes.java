package org.acme.models.enums;

public enum TransactionTypes {
    ADD(1, "ADD"),
    REMOVE(2, "REMOVE");

    private final int id;
    private final String name;

    TransactionTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TransactionTypes valueOf(Integer id) {
        if (id == null)
            return null;
        for (TransactionTypes type : TransactionTypes.values()) {
            if (type.getId() == id)
                return type;
        }
        return null;
    }
}
