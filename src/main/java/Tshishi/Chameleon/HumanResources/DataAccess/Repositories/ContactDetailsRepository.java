package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails, UUID> {
    List<ContactDetails> findAllByMail(String mail);
}
