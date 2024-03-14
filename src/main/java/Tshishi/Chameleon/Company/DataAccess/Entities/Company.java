package Tshishi.Chameleon.Company.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.Subscription;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9A-Za-z]{8,}$", message = "Numéro d'entreprise européen invalide")
    private String businessNumber;

    @OneToOne
    private ContactDetails contactDetails;

    @ManyToOne
    private Users tutors;

    @ManyToMany
    @JoinTable(
            name = "workers_users",
            joinColumns = @JoinColumn(name = "workerid"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    @ToString.Exclude
    private List<Users> workers;

    @ManyToMany
    @JoinTable(
            name = "client_users",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    @ToString.Exclude
    private List<Users> client;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "subscription_users",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    @ToString.Exclude
    private List<Subscription> subscriptions;
}
