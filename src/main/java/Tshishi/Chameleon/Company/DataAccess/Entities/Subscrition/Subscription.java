package Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition;

import Tshishi.Chameleon.Common.AbstractClass.BaseEntity;
import Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.Enum.Periodic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @ManyToOne
    private SubscriptionType subscriptionType;
    @Column
    private Periodic periodic;
    @Column
    private int duration;
    @Column
    private boolean active;
}
