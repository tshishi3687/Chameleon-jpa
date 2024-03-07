package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocalityRepository extends JpaRepository<Locality, UUID> {
    Optional<Locality> findLocalityByName(String name);

    List<Locality> findAllByNameContainingOrderByNameAsc(String locality_name);
}
