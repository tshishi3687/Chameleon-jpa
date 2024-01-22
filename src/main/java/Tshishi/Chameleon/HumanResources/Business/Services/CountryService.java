package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.AbstractClass.BaseService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.CountryMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class CountryService extends BaseService<CountryDto, UUID> {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper = new CountryMapper();
    private final static Logger logger = Logger.getLogger(CountryService.class.getName());
    private String errorMessage;

    public CountryService(CountryRepository countryRepository) {
        super(logger, CountryDto.class.getName());
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDto addEntity(CountryDto countryDto) {
        countryRepository.findCountryByName(countryDto.getName().toUpperCase())
                .ifPresentOrElse(
                        value -> {
                            countryDto.setId(value.getId());
                            countryDto.setName(value.getName());
                            logger.info(String.format("This country : \"%s\" existed.", countryDto.getName()));
                        },
                        () -> {
                            Country country = countryRepository.save(countryMapper.toEntity(countryDto));
                            countryDto.setId(country.getId());
                            countryDto.setName(country.getName());
                            logger.info(String.format("The country \"%s\" was successfully registered.", countryDto.getName()));
                        }
                );
        return countryDto;
    }

    @Override
    public CountryDto readEntity(UUID uuid) {
        CountryDto countryDto = new CountryDto();
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
        return countryMapper.toDtos(countryRepository.findAll());
    }

    @Override
    public CountryDto updateEntity(CountryDto countryDto, UUID uuid) {
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
