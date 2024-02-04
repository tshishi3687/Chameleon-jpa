package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseEntity {

    @Column
    private UUID validationCode;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String mail;

    @Column(unique = true)
    private String phone;

    @Column
    private String passWord;

    @Column
    private LocalDate birthdays;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "UsersID"),
            inverseJoinColumns = @JoinColumn(name = "RolesID"))
    @ToString.Exclude
    private List<Roles> rolesList;


    @OneToMany
    private List<ContactDetails> contactDetails;
}
