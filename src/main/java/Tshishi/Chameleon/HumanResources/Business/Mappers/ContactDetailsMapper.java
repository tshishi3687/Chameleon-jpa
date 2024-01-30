package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;

import java.util.List;
public class ContactDetailsMapper implements IdentifiedMapper<ContactDetailsDto, ContactDetails> {

    private final LocalityMapper localityMapper = new LocalityMapper();
    private final CountryMapper countryMapper = new CountryMapper();
    private final CountryRepository countryRepository;
    private final LocalityRepository localityRepository;

    public ContactDetailsMapper(CountryRepository countryRepository, LocalityRepository localityRepository) {
        this.countryRepository = countryRepository;
        this.localityRepository = localityRepository;
    }

    @Override
    public ContactDetailsDto toDto(ContactDetails contactDetails) {
        return new ContactDetailsDto(
                contactDetails.getId(),
                contactDetails.getMail(),
                contactDetails.getPhone(),
                contactDetails.getAddress(),
                contactDetails.getNumber(),
                localityMapper.toDto(contactDetails.getLocality()),
                countryMapper.toDto(contactDetails.getCountry())
        );
    }

    @Override
    public ContactDetails toEntity(ContactDetailsDto contactDetailsDto) {
        return new ContactDetails(
                contactDetailsDto.getMail(),
                contactDetailsDto.getPhone(),
                contactDetailsDto.getAddress(),
                contactDetailsDto.getNumber(),
                localityRepository.findById(contactDetailsDto.getLocality().getId()).orElseThrow(),
                countryRepository.findById(contactDetailsDto.getCountry().getId()).orElseThrow(),
                null
        );
    }

    @Override
    public List<ContactDetailsDto> toDtos(List<ContactDetails> contactDetails) {
        return IdentifiedMapper.super.toDtos(contactDetails);
    }
}
