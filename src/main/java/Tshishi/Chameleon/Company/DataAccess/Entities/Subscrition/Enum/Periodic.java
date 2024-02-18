package Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.Enum;

import lombok.Getter;

@Getter
public enum Periodic {
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY");

    private final String periodicName;

    Periodic(String roleName) {
        this.periodicName = roleName;
    }
}
