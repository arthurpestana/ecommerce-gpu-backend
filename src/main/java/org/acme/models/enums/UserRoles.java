package org.acme.models.enums;

public enum UserRoles {
    ADMIN(1, "ADMIN"),
    CUSTOMER(2, "CUSTOMER");

    private final int id;
    private final String name;

    UserRoles(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UserRoles valueOf(Integer id) {
        if (id == null)
            return null;
        for (UserRoles role : UserRoles.values()) {
            if (role.getId() == id)
                return role;
        }
        return null;
    }
}
