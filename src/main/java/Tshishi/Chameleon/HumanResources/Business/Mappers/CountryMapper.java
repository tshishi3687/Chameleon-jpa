package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;

import java.util.List;
import java.util.logging.Logger;

public class CountryMapper implements IdentifiedMapper<CountryDto, Country> {

    private final static Logger logger = Logger.getLogger(CountryMapper.class.getName());

    @Override
    public CountryDto toDto(Country country) {
        logger.info(String.format("Beginning of mapping from Entity class \"%s\" to Dto class \"%s\".", Country.class.getName(), CountryDto.class.getName()));

        return new CountryDto(
                country.getId(),
                country.getName()
        );
    }

    @Override
    public Country toEntity(CountryDto countryDto) {
        logger.info(String.format("Beginning of mapping from Dto class \"%s\" to Entity class \"%s\".", CountryDto.class.getName(), Country.class.getName()));

        return new Country(
                countryDto.getName().toUpperCase()
        );
    }

    @Override
    public List<CountryDto> toDtos(List<Country> countries) {
        logger.info(String.format("Beginning of mapping from Entities class \"%s\" to Dtos class \"%s\".", Country.class.getName(), CountryDto.class.getName()));

        return IdentifiedMapper.super.toDtos(countries);
    }
}
