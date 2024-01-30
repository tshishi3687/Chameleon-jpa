package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.CountryMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CountryService implements IdentifiedService<CountryDto, UUID> {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper = new CountryMapper();
    private final ServiceLogs serviceLogs;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.serviceLogs = new ServiceLogs();
    }

    @Override
    public CountryDto addEntity(CountryDto dto) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        countryRepository.findCountryByName(dto.getName().toUpperCase())
                .ifPresentOrElse(
                        value -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, dto, value.getId()),
                        () -> {
                            Country country = countryRepository.save(countryMapper.toEntity(dto));
                            dto.setId(country.getId());
                            dto.setName(country.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, dto, dto.getId());
                        }
                );
        return dto;
    }

    @Override
    public CountryDto readEntity(UUID uuid) {
        CountryDto dto = new CountryDto();
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto, uuid);
        countryRepository.findById(uuid)
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
    public List<CountryDto> readAllEntities() {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ALL_ENTITY, new RolesDto(), null);
        return countryMapper.toDtos(countryRepository.findAll());
    }

    @Override
    public CountryDto updateEntity(CountryDto dto, UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.UPDATING_ENTITY, dto, uuid);
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            value.setName(dto.getName());
                            Country country = countryRepository.save(value);
                            dto.setId(country.getId());
                            dto.setName(country.getName());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.UPDATING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid);
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            countryRepository.delete(value);
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.DELETING_ENTITY, new RolesDto(), value.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid)
                );
    }
}
