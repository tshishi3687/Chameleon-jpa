package Tshishi.Chameleon.Company.DataAccess.Repository;

import Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, UUID> {
}
