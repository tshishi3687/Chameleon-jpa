package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import Tshishi.Chameleon.Company.DataAccess.Entities.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Roles extends BaseEntity {

    @Column(unique = true)
    private String name;

    @OneToOne
    private Company company;

    @ManyToMany(mappedBy = "rolesList")
    private List<Users> usersList;
}
