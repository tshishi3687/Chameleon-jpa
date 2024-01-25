package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.ContactDetailsMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.ContactDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
public class ContactDetailsService implements IdentifiedService<ContactDetailsDto, UUID> {

    private final ContactDetailsRepository contactDetailsRepository;
    private final static Logger logger = Logger.getLogger(ContactDetailsService.class.getSimpleName());
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();
    private String errorMessage;

    public ContactDetailsService(ContactDetailsRepository contactDetailsRepository) {
        this.contactDetailsRepository = contactDetailsRepository;
    }

    @Override
    public ContactDetailsDto addEntity(ContactDetailsDto dto) {
        new ServiceStarterLogs<>(logger, dto).ifAddingEntity();
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
        new ServiceStarterLogs<>(logger, contactDetailsDto.get()).ifReadingEntity(uuid);
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
        new ServiceStarterLogs<>(logger, new ContactDetailsDto()).ifReadingAllEntity();
        return contactDetailsMapper.toDtos(contactDetailsRepository.findAll());
    }

    @Override
    public ContactDetailsDto updateEntity(ContactDetailsDto dto, UUID uuid) {
        new ServiceStarterLogs<>(logger, dto).ifUpdatingEntity(uuid);
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
        new ServiceStarterLogs<>(logger, new CountryDto()).ifDeletingEntity(uuid);
    }
}
