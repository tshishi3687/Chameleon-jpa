package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.CountryMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class CountryService implements IdentifiedService<CountryDto, UUID> {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper = new CountryMapper();
    private final static Logger logger = Logger.getLogger(CountryService.class.getSimpleName());
    private String errorMessage;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDto addEntity(CountryDto dto) {
        new ServiceStarterLogs<>(logger, dto).ifAddingEntity();
        countryRepository.findCountryByName(dto.getName().toUpperCase())
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            logger.info(String.format("This country : \"%s\" existed.", dto.getName()));
                        },
                        () -> {
                            Country country = countryRepository.save(countryMapper.toEntity(dto));
                            dto.setId(country.getId());
                            dto.setName(country.getName());
                            logger.info(String.format("The country \"%s\" was successfully registered.", dto.getName()));
                        }
                );
        return dto;
    }

    @Override
    public CountryDto readEntity(UUID uuid) {
        CountryDto countryDto = new CountryDto();
        new ServiceStarterLogs<>(logger, countryDto).ifReadingEntity(uuid);
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            countryDto.setId(value.getId());
                            countryDto.setName(value.getName());
                            logger.info(String.format("This country with id : %s was found and sent.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This country with id  : %s NOT FOUND!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return countryDto;
    }

    @Override
    public List<CountryDto> readAllEntities() {
        new ServiceStarterLogs<>(logger, new CountryDto()).ifReadingAllEntity();
        return countryMapper.toDtos(countryRepository.findAll());
    }

    @Override
    public CountryDto updateEntity(CountryDto countryDto, UUID uuid) {
        new ServiceStarterLogs<>(logger, countryDto).ifUpdatingEntity(uuid);
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            logger.info(String.format("This country with id : %s was found.", uuid));
                            value.setName(countryDto.getName());
                            Country country = countryRepository.save(value);
                            countryDto.setId(country.getId());
                            countryDto.setName(country.getName());
                            logger.info(String.format("This country with id : %s was UPDATED.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This country with id : %s was not found. UPDATE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return countryDto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        new ServiceStarterLogs<>(logger, new CountryDto()).ifDeletingEntity(uuid);
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            countryRepository.delete(value);
                            logger.info(String.format("This country with id : %s was found and deleted.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This country with id : %s was not found. DELETE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
