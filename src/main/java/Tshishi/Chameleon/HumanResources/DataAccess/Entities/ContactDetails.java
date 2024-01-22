package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetails extends BaseEntity {

    @Column(unique = true)
    private String mail;

    @Column(unique = true)
    private String phone;

    @Column
    private String address;

    @Column
    private String number;

    @ManyToOne
    private Locality locality;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Users users;
}
