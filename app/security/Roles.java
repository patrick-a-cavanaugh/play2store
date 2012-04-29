package security;

import be.objectify.deadbolt.models.Role;

public enum Roles implements Role {

    ADMIN("admin");

    private String roleName;

    private Roles(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getRoleName() {
        return roleName;
    }
}
