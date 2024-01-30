package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.ContactDetailsMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.ContactDetailsRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
public class ContactDetailsService implements IdentifiedService<ContactDetailsDto, UUID> {

    private final ContactDetailsRepository contactDetailsRepository;
    private final static Logger logger = Logger.getLogger(ContactDetailsService.class.getSimpleName());
    private final ContactDetailsMapper contactDetailsMapper;
    private final ServiceLogs serviceLogs;

    public ContactDetailsService(ContactDetailsRepository contactDetailsRepository, CountryRepository countryRepository, LocalityRepository localityRepository) {
        this.contactDetailsRepository = contactDetailsRepository;
        this.serviceLogs = new ServiceLogs();
        this.contactDetailsMapper = new ContactDetailsMapper(countryRepository, localityRepository);
    }

    @Override
    public ContactDetailsDto addEntity(ContactDetailsDto dto) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
//        if (contactDetailsRepository.countAllByUsers_Id(dto.getUsers().getId()) >= 3) {
//            String errorMessage = String.format("The users with id : %s already has 3 contact Details saved. MAXIMUM AUTHORIZED IS 3 FOR USERS", dto.getUsers().getId());
//            logger.warning(errorMessage);
//            throw new UnsupportedOperationException(errorMessage);
//        }

        logger.info("Inject elements : locality, country, users.");
        ContactDetails contactDetails = contactDetailsMapper.toEntity(dto);
        //        logger.info(String.format("This users with id : %s was be a new contact details.", dto.getUsers().getId()));
        return contactDetailsMapper.toDto(contactDetailsRepository.save(contactDetails));
    }

    @Override
    public ContactDetailsDto readEntity(UUID uuid) {
        AtomicReference<ContactDetailsDto> dto = new AtomicReference<>();
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto.get(), uuid);
        contactDetailsRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            dto.set(contactDetailsMapper.toDto(value));
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.READING_ENTITY, dto.get(), dto.get().getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto.get(), uuid)
                );
        return dto.get();
    }

    @Override
    public List<ContactDetailsDto> readAllEntities() {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ALL_ENTITY, new RolesDto(), null);
        return contactDetailsMapper.toDtos(contactDetailsRepository.findAll());
    }

    @Override
    public ContactDetailsDto updateEntity(ContactDetailsDto dto, UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.UPDATING_ENTITY, dto, uuid);
        contactDetailsRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            ContactDetails contactDetails = contactDetailsMapper.toEntity(dto);
                            dto.setId(contactDetailsRepository.save(contactDetails).getId());
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, dto, dto.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.UPDATING_ENTITY, dto, uuid)
                );
        return dto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid);
        contactDetailsRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            contactDetailsRepository.delete(value);
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.DELETING_ENTITY, new ContactDetailsDto(), uuid);
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.DELETING_ENTITY, new RolesDto(), uuid)
                );
    }
}
