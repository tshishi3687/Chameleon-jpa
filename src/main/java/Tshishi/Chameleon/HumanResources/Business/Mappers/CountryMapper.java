package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;

import java.util.List;

public class CountryMapper implements IdentifiedMapper<CountryDto, Country> {
    @Override
    public CountryDto toDto(Country country) {
        return new CountryDto(
                country.getId(),
                country.getName()
        );
    }

    @Override
    public Country toEntity(CountryDto countryDto) {
        return new Country(
                countryDto.getName().toUpperCase()
        );
    }

    @Override
    public List<CountryDto> toDtos(List<Country> countries) {
        return IdentifiedMapper.super.toDtos(countries);
    }
}
