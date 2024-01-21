package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Users extends BaseEntity {

    @Column
    private UUID validationCode;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String passWord;

    @Column
    private LocalDate birthdays;

    @ManyToMany
    @JoinTable(
            name = "users-roles",
            joinColumns = @JoinColumn(name = "UsersID"),
            inverseJoinColumns = @JoinColumn(name = "RolesID"))
    @ToString.Exclude
    private List<Roles> rolesList;

    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name="contact_details", referencedColumnName = "id")
    private ContactDetails contactDetails;
}
