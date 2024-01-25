package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.RolesMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RolesService implements IdentifiedService<RolesDto, UUID> {

    private final static Logger logger = Logger.getLogger(RolesService.class.getSimpleName());
    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper = new RolesMapper();
    private String errorMessage;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public RolesDto addEntity(RolesDto dto) {
        new ServiceStarterLogs<>(logger, dto).ifAddingEntity();
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
        new ServiceStarterLogs<>(logger, rolesDto).ifReadingEntity(uuid);
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesDto.setId(value.getId());
                            rolesDto.setName(value.getName());
                            logger.info(String.format("%s with id : %s was found and sent", value.getClass().getSimpleName(), uuid));
                        },
                        () -> {
                            errorMessage = String.format("%s with id : %s NOT FOUND!", "This roles", uuid);
                            logger.severe(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return rolesDto;
    }

    @Override
    public List<RolesDto> readAllEntities() {
        new ServiceStarterLogs<>(logger, new RolesDto()).ifReadingAllEntity();
        return rolesMapper.toDtos(rolesRepository.findAll());
    }

    @Override
    public RolesDto updateEntity(RolesDto rolesDto, UUID uuid) {
        new ServiceStarterLogs<>(logger, rolesDto).ifUpdatingEntity(uuid);
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
                            logger.severe(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return rolesDto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        new ServiceStarterLogs<>(logger, new RolesDto()).ifDeletingEntity(uuid);
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesRepository.delete(value);
                            logger.info(String.format("%s with id : %s was found and deleted.",value.getClass().getSimpleName(), uuid));
                        },
                        () -> {
                            errorMessage = String.format("%s with id : %s was not found. DELETE FAIL!", "This Roles", uuid);
                            logger.severe(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
