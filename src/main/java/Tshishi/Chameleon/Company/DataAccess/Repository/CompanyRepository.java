package Tshishi.Chameleon.Company.DataAccess.Repository;

import Tshishi.Chameleon.Company.DataAccess.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
    @Query("SELECT DISTINCT c FROM Company c " +
            "LEFT JOIN c.tutors t " +
            "LEFT JOIN c.workers w on w.Id = c.Id " +
            "LEFT JOIN c.client cl on cl.Id = c.Id " +
            "WHERE t.Id = :userId OR w.Id = :userId OR cl.Id = :userId")
    List<Company> findCompaniesByUserId(@Param("userId") UUID userId);
}
