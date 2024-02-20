package Tshishi.Chameleon.Company.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.Subscription;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import jakarta.persistence.*;
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

    @OneToOne
    private ContactDetails contactDetails;

    @ManyToMany
    @JoinTable(
            name = "company_users",
            joinColumns = @JoinColumn(name = "companyId"),
            inverseJoinColumns = @JoinColumn(name = "usersId"))
    @ToString.Exclude
    private List<Users> tutors;

    @ManyToMany
    @JoinTable(
            name = "workers_users",
            joinColumns = @JoinColumn(name = "workerid"),
            inverseJoinColumns = @JoinColumn(name = "usersId"))
    @ToString.Exclude
    private List<Users> workers;

    @ManyToMany
    @JoinTable(
            name = "client_users",
            joinColumns = @JoinColumn(name = "clientId"),
            inverseJoinColumns = @JoinColumn(name = "usersId"))
    @ToString.Exclude
    private List<Users> client;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "subscription_users",
            joinColumns = @JoinColumn(name = "subscriptionId"),
            inverseJoinColumns = @JoinColumn(name = "usersId"))
    @ToString.Exclude
    private List<Subscription> subscriptions;
}
