package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;

import java.util.List;
public class ContactDetailsMapper implements IdentifiedMapper<ContactDetailsDto, ContactDetails> {

    private final LocalityMapper localityMapper = new LocalityMapper();
    private final CountryMapper countryMapper = new CountryMapper();

    @Override
    public ContactDetailsDto toDto(ContactDetails contactDetails) {
        return new ContactDetailsDto(
                contactDetails.getId(),
                contactDetails.getAddress(),
                contactDetails.getNumber(),
                localityMapper.toDto(contactDetails.getLocality()),
                countryMapper.toDto(contactDetails.getCountry())
        );
    }

    @Override
    public ContactDetails toEntity(ContactDetailsDto contactDetailsDto) {
        return new ContactDetails(
                contactDetailsDto.getAddress(),
                contactDetailsDto.getNumber(),
                contactDetailsDto.getLocality() != null? localityMapper.toEntity(contactDetailsDto.getLocality()): null,
                contactDetailsDto.getCountry() != null? countryMapper.toEntity(contactDetailsDto.getCountry()): null
        );
    }

    @Override
    public List<ContactDetailsDto> toDtos(List<ContactDetails> contactDetails) {
        return IdentifiedMapper.super.toDtos(contactDetails);
    }
}
