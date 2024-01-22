package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.AbstractClass.BaseService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.RolesMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RolesService extends BaseService<RolesDto, UUID> {

    private final static Logger logger = Logger.getLogger(RolesService.class.getName());
    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper = new RolesMapper();
    private String errorMessage;

    public RolesService(RolesRepository rolesRepository) {
        super(logger, RolesDto.class.getName());
        this.rolesRepository = rolesRepository;
    }

    @Override
    public RolesDto addEntity(RolesDto rolesDto) {
        rolesRepository.findRolesByName(rolesDto.getName())
                .ifPresentOrElse(
                        value -> {
                            rolesDto.setId(value.getId());
                            rolesDto.setName(value.getName());
                            logger.info(String.format("This roles : \"%s\" existed.", rolesDto.getName()));
                        },
                        () -> {
                            Roles roles = rolesRepository.save(rolesMapper.toEntity(rolesDto));
                            rolesDto.setId(roles.getId());
                            rolesDto.setName(roles.getName());
                            logger.info(String.format("The roles \"%s\" was successfully registered.", rolesDto.getName()));
                        }
                );
        return rolesDto;
    }

    @Override
    public RolesDto readEntity(UUID uuid) {
        RolesDto rolesDto = new RolesDto();
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesDto.setId(value.getId());
                            rolesDto.setName(value.getName());
                            logger.info(String.format("This roles with id : %s was found and sent", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This roles with id : %s NOT FOUND!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return rolesDto;
    }

    @Override
    public List<RolesDto> readAllEntities() {
        return rolesMapper.toDtos(rolesRepository.findAll());
    }

    @Override
    public RolesDto updateEntity(RolesDto rolesDto, UUID uuid) {

        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            logger.info(String.format("This roles with id : %s was found", uuid));
                            value.setName(rolesDto.getName().toUpperCase());
                            Roles roles = rolesRepository.save(value);
                            rolesDto.setId(roles.getId());
                            rolesDto.setName(roles.getName());
                            logger.info(String.format("This roles with id : %s was UPDATED.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This roles with id : %s was not found. UPDATE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return rolesDto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesRepository.delete(value);
                            logger.info(String.format("This roles with id : %s was found and deleted.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This roles with id : %s was not found. DELETE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
