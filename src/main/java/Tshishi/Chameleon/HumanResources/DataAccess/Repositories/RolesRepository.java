package Tshishi.Chameleon.HumanResources.DataAccess.Repositories;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolesRepository extends JpaRepository<Roles, UUID> {
}
