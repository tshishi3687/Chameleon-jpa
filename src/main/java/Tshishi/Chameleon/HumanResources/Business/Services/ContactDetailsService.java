package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.AbstractClass.BaseService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.ContactDetailsMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.ContactDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
public class ContactDetailsService extends BaseService<ContactDetailsDto, UUID> {

    private final ContactDetailsRepository contactDetailsRepository;
    private final static Logger logger = Logger.getLogger(ContactDetailsService.class.getName());
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();

    public ContactDetailsService(ContactDetailsRepository contactDetailsRepository) {
        super(logger, ContactDetailsDto.class.getName());
        this.contactDetailsRepository = contactDetailsRepository;
    }

    @Override
    public ContactDetailsDto addEntity(ContactDetailsDto dto) {
        if (contactDetailsRepository.countAllByUsers_Id(dto.getUsers().getId()) >= 3) {
            errorMessage = String.format("The users with id : %s already has 3 contact Details saved. MAXIMUM AUTHORIZED IS 3 FOR USERS", dto.getUsers().getId());
            logger.warning(errorMessage);
            throw new UnsupportedOperationException(errorMessage);
        }

        logger.info("Inject elements : locality, country, users.");
        ContactDetails contactDetails = contactDetailsMapper.toEntity(dto);
        ContactDetailsDto contactDetailsDto = contactDetailsMapper.toDto(contactDetailsRepository.save(contactDetails));
        logger.info(String.format("This users with id : %s was be a new contact details.", dto.getUsers().getId()));
        return contactDetailsDto;
    }

    @Override
    public ContactDetailsDto readEntity(UUID uuid) {
        AtomicReference<ContactDetailsDto> contactDetailsDto = new AtomicReference<>();
        contactDetailsRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            contactDetailsDto.set(contactDetailsMapper.toDto(value));
                            logger.info(String.format("This users with id : %s was found and sent.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This users with id  : %s NOT FOUND!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return contactDetailsDto.get();
    }

    @Override
    public List<ContactDetailsDto> readAllEntities() {
        return contactDetailsMapper.toDtos(contactDetailsRepository.findAll());
    }

    @Override
    public ContactDetailsDto updateEntity(ContactDetailsDto dto, UUID uuid) {
        contactDetailsRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            logger.info(String.format("This contact details with id : %s was found.", uuid));
                            ContactDetails contactDetails = contactDetailsMapper.toEntity(dto);
                            dto.setId(contactDetailsRepository.save(contactDetails).getId());
                            logger.info(String.format("This contact details with id : %s was UPDATED.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This contact details with id : %s was not found. UPDATE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        super.deleteEntity(uuid);
    }
}
