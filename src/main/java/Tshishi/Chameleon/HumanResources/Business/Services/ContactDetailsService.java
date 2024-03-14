package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.ContactDetailsMapper;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Country;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.ContactDetailsRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.CountryRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ContactDetailsService implements IdentifiedService<ContactDetailsDto, UUID> {

    private final ContactDetailsRepository contactDetailsRepository;
    private final LocalityRepository localityRepository;
    private final CountryRepository countryRepository;
    private final static Logger logger = Logger.getLogger(ContactDetailsService.class.getSimpleName());
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();
    private final ServiceLogs serviceLogs = new ServiceLogs();


    @Override
    public ContactDetailsDto addEntity(ContactDetailsDto dto) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        logger.info("Inject elements : locality, country, users.");
        ContactDetails contactDetails = contactDetailsMapper.toEntity(dto);

        // set locality
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto.getLocality(), dto.getLocality().getId());
        localityRepository.findLocalityByName(dto.getLocality().getName().toUpperCase())
                .ifPresentOrElse(
                        contactDetails::setLocality,
                        () -> contactDetails.setLocality(localityRepository.save(new Locality(dto.getLocality().getName().toUpperCase())))
                );

        // set Country
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, dto.getCountry(), dto.getCountry().getId());
        countryRepository.findCountryByName(dto.getCountry().getName().toUpperCase())
                .ifPresentOrElse(
                        contactDetails::setCountry,
                        () -> contactDetails.setCountry(countryRepository.save(new Country(dto.getCountry().getName().toUpperCase())))
                );

        ContactDetails contactDetails1 = contactDetailsRepository.saveAndFlush(contactDetails);
        return contactDetailsMapper.toDto(contactDetails1);
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

        return null;
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
