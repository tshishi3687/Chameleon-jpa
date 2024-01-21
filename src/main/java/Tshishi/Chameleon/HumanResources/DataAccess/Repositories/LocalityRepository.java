package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocalityRepository extends JpaRepository<Locality, UUID> {
}
