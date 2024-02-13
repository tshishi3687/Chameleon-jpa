package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9A-Za-z]{8,}$", message = "Numéro d'entreprise européen invalide")
    private String businessNumber;

    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Adresse e-mail invalide")
    private String mail;

    @Column(unique = true)
    @Pattern(regexp = "^(\\+|00)\\d{1,4}[\\s/0-9]*$", message = "Numéro de téléphone européen invalide")
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

    @Column
    private boolean active;
}
