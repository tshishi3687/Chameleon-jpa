package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;

import java.util.List;
import java.util.logging.Logger;

public class ContactDetailsMapper implements IdentifiedMapper<ContactDetailsDto, ContactDetails> {

    private final static Logger logger = Logger.getLogger(ContactDetailsMapper.class.getName());
    @Override
    public ContactDetailsDto toDto(ContactDetails contactDetails) {
        logger.info(String.format("Beginning of mapping from Entity class \"%s\" to Dto class \"%s\".",ContactDetails.class.getName(), ContactDetailsDto.class.getName()));

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
        logger.info(String.format("Beginning of mapping from Dto class \"%s\" to Entity class \"%s\".",ContactDetailsDto.class.getName(), ContactDetails.class.getName()));

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
        logger.info(String.format("Beginning of mapping from Entities class \"%s\" to Dtos class \"%s\".",ContactDetails.class.getName(), ContactDetailsDto.class.getName()));

        return IdentifiedMapper.super.toDtos(contactDetails);
    }
}