package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.LocalityMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocalityService implements IdentifiedService<LocalityDto, UUID> {

    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper = new LocalityMapper();
    private final ServiceStarterLogs serviceStarterLogs;

    public LocalityService(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
        this.serviceStarterLogs = new ServiceStarterLogs();
    }

    @Override
    public LocalityDto addEntity(LocalityDto dto) {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        localityRepository.findLocalityByName(dto.getName())
                .ifPresentOrElse(
                        value -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, dto, value.getId()),
                        () -> {
                            Locality locality = localityRepository.save(localityMapper.toEntity(dto));
                            dto.setId(locality.getId());
                            dto.setName(locality.getName());
                            serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, dto, dto.getId());
                        }
                );
        return dto;
    }

    @Override
    public LocalityDto readEntity(UUID uuid) {
        LocalityDto dto = new LocalityDto();
        serviceStarterLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto, uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS,LoggerTypes.READING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR,LoggerTypes.READING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public List<LocalityDto> readAllEntities() {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.READING_ALL_ENTITY, new RolesDto(), null);
        return localityMapper.toDtos(localityRepository.findAll());
    }

    @Override
    public LocalityDto updateEntity(LocalityDto dto, UUID uuid) {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.UPDATING_ENTITY, dto, uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            value.setName(dto.getName().toUpperCase());
                            localityRepository.save(value);
                            serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS,LoggerTypes.UPDATING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR,LoggerTypes.UPDATING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            localityRepository.delete(value);
                            serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.DELETING_ENTITY, new RolesDto(), value.getId());
                        },
                        () -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid)
                );
    }
}
