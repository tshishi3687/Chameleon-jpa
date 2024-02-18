package Tshishi.Chameleon.Company.DataAccess.Repository;

import Tshishi.Chameleon.Company.DataAccess.Entities.Subscrition.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
