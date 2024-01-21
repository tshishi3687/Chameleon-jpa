package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Roles extends BaseEntity {

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "rolesList")
    private List<Users> usersList;
}
