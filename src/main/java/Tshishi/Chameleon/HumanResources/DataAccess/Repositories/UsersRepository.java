package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
}
