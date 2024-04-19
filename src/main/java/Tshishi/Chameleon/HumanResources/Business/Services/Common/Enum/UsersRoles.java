package Tshishi.Chameleon.HumanResources.Business.Services.Common.Enum;

import lombok.Getter;

@Getter
public enum UsersRoles {
    SUPER_ADMIN("SUPER ADMIN"),
    ADMIN("ADMIN"),
    WORKER("WORKER"),
    CUSTOMER("CUSTOMER");

    private final String roleName;

    UsersRoles(String roleName) {
        this.roleName = roleName;
    }

}

