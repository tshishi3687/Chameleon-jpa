package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;

import java.util.List;

public class ContactDetailsMapper implements IdentifiedMapper<ContactDetailsDto, ContactDetails> {
    @Override
    public ContactDetailsDto toDto(ContactDetails contactDetails) {
        return new ContactDetailsDto(
                contactDetails.getId(),
                contactDetails.getMail(),
                contactDetails.getPhone(),
                contactDetails.getAddress(),
                contactDetails.getNumber(),
                null,
                null
        );
    }

    @Override
    public ContactDetails toEntity(ContactDetailsDto contactDetailsDto) {
        return new ContactDetails(
                contactDetailsDto.getMail(),
                contactDetailsDto.getPhone(),
                contactDetailsDto.getAddress(),
                contactDetailsDto.getNumber(),
                null,
                null,
                null
        );
    }

    @Override
    public List<ContactDetailsDto> toDtos(List<ContactDetails> contactDetails) {
        return IdentifiedMapper.super.toDtos(contactDetails);
    }
}
