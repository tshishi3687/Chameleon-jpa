package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.CountryMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CountryService implements IdentifiedService<CountryDto, UUID> {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper = new CountryMapper();
    private final static Logger logger = Logger.getLogger(CountryService.class.getName());
    private String errorMessage;

    @Override
    public CountryDto addEntity(CountryDto countryDto) {
        logger.info(String.format("Try to add country with name %s", countryDto.getName()));

        if (StringUtils.isBlank(countryDto.getName())) {
            errorMessage = "Country name can't be null";
            logger.warning(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        countryRepository.findCountryByName(countryDto.getName().toUpperCase())
                .ifPresentOrElse(
                        value -> {
                            countryDto.setId(value.getId());
                            countryDto.setName(value.getName());
                            logger.info("This country existed");
                        },
                        () -> {
                            Country country = countryRepository.save(countryMapper.toEntity(countryDto));
                            countryDto.setId(country.getId());
                            countryDto.setName(country.getName());
                            logger.info(String.format("The country %s was successfully registered", countryDto.getName()));
                        });
        return countryDto;
    }

    @Override
    public CountryDto readEntity(UUID uuid) {
        logger.info(String.format("Try to get country with id : %s", uuid));

        CountryDto countryDto = new CountryDto();
        countryRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            countryDto.setId(value.getId());
                            countryDto.setName(value.getName());
                            logger.info(String.format("This Country with id : %s was found and sent", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This Country with id  : %s NOT FOUND", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return countryDto;
    }

    @Override
    public List<CountryDto> readAllEntity() {
        logger.info("Get all country");

        return countryMapper.toDtos(countryRepository.findAll());
    }

    @Override
    public CountryDto updateEntity(CountryDto countryDto, UUID uuid) {
        logger.info(String.format("Try to update country with id : %s", uuid));

        countryRepository.findById(uuid).ifPresentOrElse(
                value -> {
                    logger.info(String.format("Country with id : %s was found", uuid));
                    countryDto.setId(value.getId());
                    countryDto.setName(countryDto.getName().toUpperCase());
                    countryRepository.save(countryMapper.toEntity(countryDto));
                },
                () -> {
                    errorMessage = String.format("Country with id : %s was not found. UPDATE FAIL!", uuid);
                    logger.warning(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
        );
        return countryDto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        logger.info(String.format("Try to delete country with id : %s", uuid));

        countryRepository.findById(uuid).ifPresentOrElse(
                value -> {
                    countryRepository.deleteById(uuid);
                    logger.info(String.format("Country with id : %s was found and deleted", uuid));
                },
                () -> {
                    errorMessage = String.format("Country with id : %s was not found. DELETE FAIL!", uuid);
                    logger.warning(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
        );
    }
}
