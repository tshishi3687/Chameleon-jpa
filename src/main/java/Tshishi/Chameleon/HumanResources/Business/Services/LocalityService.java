package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.LocalityMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalityService implements IdentifiedService<LocalityDto, UUID> {

    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper = new LocalityMapper();
    private final ServiceLogs serviceLogs = new ServiceLogs();

    @Override
    public LocalityDto addEntity(LocalityDto dto) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        localityRepository.findLocalityByName(dto.getName())
                .ifPresentOrElse(
                        value -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, dto, value.getId()),
                        () -> {
                            Locality locality = localityRepository.save(localityMapper.toEntity(dto));
                            dto.setId(locality.getId());
                            dto.setName(locality.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, dto, dto.getId());
                        }
                );
        return dto;
    }

    @Override
    public LocalityDto readEntity(UUID uuid) {
        LocalityDto dto = new LocalityDto();
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto, uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS,LoggerTypes.READING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR,LoggerTypes.READING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public List<LocalityDto> readAllEntities() {
        serviceLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.READING_ALL_ENTITY, new LocalityDto(), null);
        return localityMapper.toDtos(localityRepository.findAll());
    }

    public List<LocalityDto> readAllEntitiesWhichContainsString(String search) {
        serviceLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.READING_ALL_ENTITY, new LocalityDto(), null);
        return localityMapper.toDtos(localityRepository.findAllByNameContainingOrderByNameAsc(search));
    }

    @Override
    public LocalityDto updateEntity(LocalityDto dto, UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.UPDATING_ENTITY, dto, uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            value.setName(dto.getName().toUpperCase());
                            localityRepository.save(value);
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS,LoggerTypes.UPDATING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR,LoggerTypes.UPDATING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY,LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            localityRepository.delete(value);
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.DELETING_ENTITY, new RolesDto(), value.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid)
                );
    }
}
