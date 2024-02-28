package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.RolesMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolesService implements IdentifiedService<RolesDto, UUID> {

    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper = new RolesMapper();
    private final ServiceLogs serviceLogs = new ServiceLogs();

    @Override
    public RolesDto addEntity(RolesDto dto) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        rolesRepository.findRolesByName(dto.getName())
                .ifPresentOrElse(
                        value -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, dto, value.getId()),
                        () -> {
                            Roles roles = rolesRepository.save(rolesMapper.toEntity(dto));
                            dto.setId(roles.getId());
                            dto.setName(roles.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, dto, dto.getId());
                        }
                );
        return dto;
    }

    @Override
    public RolesDto readEntity(UUID uuid) {
        RolesDto dto = new RolesDto();
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto, uuid);
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.READING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public List<RolesDto> readAllEntities() {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ALL_ENTITY, new RolesDto(), null);
        return rolesMapper.toDtos(rolesRepository.findAll());
    }

    @Override
    public RolesDto updateEntity(RolesDto dto, UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.UPDATING_ENTITY, dto, uuid);
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            value.setName(dto.getName().toUpperCase());
                            Roles roles = rolesRepository.save(value);
                            dto.setId(roles.getId());
                            dto.setName(roles.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.UPDATING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid);
        rolesRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            rolesRepository.delete(value);
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.DELETING_ENTITY, new RolesDto(), value.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid)
                );
    }
}
