package Tshishi.Chameleon.Company.DataAccess.Repository;

import Tshishi.Chameleon.Company.DataAccess.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
}
