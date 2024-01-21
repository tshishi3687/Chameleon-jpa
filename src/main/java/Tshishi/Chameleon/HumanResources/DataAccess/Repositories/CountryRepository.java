package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
}
