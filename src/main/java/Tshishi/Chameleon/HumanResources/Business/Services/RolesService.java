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

    private final static Logger logger = Logger.getLogger(RolesService.class.getSimpleName());
    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper = new RolesMapper();
    private String errorMessage;

    public RolesService(RolesRepository rolesRepository) {
        super(logger, RolesDto.class.getSimpleName());
        this.rolesRepository = rolesRepository;
    }

    @Override
    public RolesDto addEntity(RolesDto dto) {
        rolesRepository.findRolesByName(dto.getName())
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            logger.warning(String.format("%s : \"%s\" existed with id : %s.", value.getClass().getSimpleName(), dto.getName(), dto.getId()));
                        },
                        () -> {
                            Roles roles = rolesRepository.save(rolesMapper.toEntity(dto));
                            dto.setId(roles.getId());
                            dto.setName(roles.getName());
                            logger.info(String.format("The roles \"%s\" was successfully registered.", dto.getClass().getSimpleName()));
                        }
                );
        return dto;
    }

    @Override
    public RolesDto readEntity(UUID uuid) {
        RolesDto rolesDto = new RolesDto();
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesDto.setId(value.getId());
                            rolesDto.setName(value.getName());
                            logger.info(String.format("%s with id : %s was found and sent", value.getClass().getSimpleName(), uuid));
                        },
                        () -> {
                            errorMessage = String.format("%s with id : %s NOT FOUND!", "This Roles.", uuid);
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
                            logger.info(String.format("%s with id : %s was found", value.getClass().getSimpleName(), uuid));
                            value.setName(rolesDto.getName().toUpperCase());
                            Roles roles = rolesRepository.save(value);
                            rolesDto.setId(roles.getId());
                            rolesDto.setName(roles.getName());
                            logger.info(String.format("%s with id : %s was UPDATED.", value.getClass().getSimpleName(), uuid));
                        },
                        () -> {
                            errorMessage = String.format("%s with id : %s was not found. UPDATE FAIL!", "This Roles", uuid);
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
                            logger.info(String.format("%s with id : %s was found and deleted.",value.getClass().getSimpleName(), uuid));
                        },
                        () -> {
                            errorMessage = String.format("%s with id : %s was not found. DELETE FAIL!", "This Roles", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
