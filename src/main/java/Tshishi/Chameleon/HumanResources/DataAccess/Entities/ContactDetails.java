package Tshishi.Chameleon.HumanResources.DataAccess.Entities;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @OneToOne(mappedBy = "contactDetails")
    private Users users;
}
