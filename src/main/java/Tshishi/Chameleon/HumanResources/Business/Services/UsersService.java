package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.HumanResources.Business.Dtos.*;
import Tshishi.Chameleon.HumanResources.Business.Mappers.*;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Enum.UsersRoles;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.*;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.*;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UsersService {

    private final UsersCreatedMapper usersCreatedMapper = new UsersCreatedMapper();
    private final UsersVueMapper usersVueMapper = new UsersVueMapper();
    private final UsersRepository usersRepository;
    private final ServiceLogs serviceLogs;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RolesRepository rolesRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final ContactDetailsMapper contactDetailsMapper;
    private final LocalityRepository localityRepository;
    private final CountryRepository countryRepository;


    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RolesRepository rolesRepository, ContactDetailsRepository contactDetailsRepository, LocalityRepository localityRepository, CountryRepository countryRepository) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.rolesRepository = rolesRepository;
        this.contactDetailsRepository = contactDetailsRepository;
        this.contactDetailsMapper = new ContactDetailsMapper();
        this.localityRepository = localityRepository;
        this.countryRepository = countryRepository;
        this.serviceLogs = new ServiceLogs();
    }

    @Transactional
    public UsersVueDto addEntity(UsersCreatedDto usersCreatedDto) {
        AtomicReference<Users> usersAtomicReference = new AtomicReference<>();
        usersRepository.findUsersByMailOrPhoneOrBusinessNumber(usersCreatedDto.getMail(), usersCreatedDto.getPhone(), usersCreatedDto.getBusinessNumber())
                .ifPresentOrElse(
                        value -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, usersCreatedDto, value.getId()),
                        () -> {
                            List<Roles> rolesList = rolesRepository.findAll();

                            usersAtomicReference.set(usersCreatedMapper.toEntity(usersCreatedDto));

                            // Check roles
                            if (rolesList.isEmpty()) {
                                serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.FIRST_CONNECT, usersCreatedDto, null);
                                serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, usersCreatedDto.getRolesDtoList().stream().findFirst().orElseThrow(), null);
                                // First user using app
                                Roles roles = new Roles();
                                roles.setName(UsersRoles.SUPER_ADMIN.getRoleName());
                                usersAtomicReference.get().getRolesList().add(rolesRepository.save(roles));
                                serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, new RolesDto(), usersAtomicReference.get().getRolesList().stream().findFirst().orElseThrow().getId());
                            } else {
                                serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, usersCreatedDto, null);
                                // Check if rolesDto not existed
                                usersCreatedDto.getRolesDtoList().forEach(rolesDto -> {
                                    if (!rolesRepository.existsById(rolesDto.getId())) {
                                        serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, rolesDto, rolesDto.getId());
                                    }
                                });
                            }

                            // Crypte password
                            usersAtomicReference.get().setPassWord(bCryptPasswordEncoder.encode(usersCreatedDto.getPassword()));

                            // Add roles
                            usersAtomicReference.get().getRolesList().addAll(
                                    rolesRepository.findAllByIdIn(
                                            usersCreatedDto.getRolesDtoList().stream()
                                                    .filter(rolesDto -> !rolesDto.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName()))
                                                    .map(RolesDto::getId).toList()
                                    )
                            );

                            // creat success logs
                            usersAtomicReference.get().getRolesList().forEach(roles -> serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.READING_ENTITY, new RolesDto(), roles.getId()));

                            // add contact details
                            List<ContactDetails> contactDetailsList = new ArrayList<>();
                            usersCreatedDto.getContactDetails().forEach(
                                    contactDetailsDto -> {
                                        // mapping contact detail
                                        ContactDetails contactDetails = contactDetailsMapper.toEntity(contactDetailsDto);

                                        // set locality
                                        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, contactDetailsDto.getLocality(), contactDetailsDto.getLocality().getId());
                                        contactDetails.setLocality(localityRepository.findById(contactDetailsDto.getLocality().getId())
                                                .orElse(localityRepository.save(new Locality(contactDetailsDto.getLocality().getName().toUpperCase())))
                                        );

                                        // set Country
                                        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, contactDetailsDto.getCountry(), contactDetailsDto.getCountry().getId());
                                        contactDetails.setCountry(countryRepository.findById(contactDetailsDto.getCountry().getId())
                                                .orElse(countryRepository.save(new Country(contactDetailsDto.getCountry().getName().toUpperCase())))
                                        );

                                        // set and add contact details
                                        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, contactDetailsMapper.toDto(contactDetails), null);
                                        ContactDetails contactDetails1 = contactDetailsRepository.save(contactDetails);

                                        contactDetailsList.add(contactDetails1);

                                        // set success logs
                                        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, new LocalityDto(), contactDetails.getLocality().getId());
                                        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, new CountryDto(), contactDetails.getCountry().getId());
                                        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, new ContactDetailsDto(), contactDetails1.getId());
                                    }
                            );
                            usersAtomicReference.get().setContactDetails(contactDetailsList);
                        }
                );
        UsersVueDto usersVueDto = usersVueMapper.toDto(usersRepository.save(usersAtomicReference.get()));
        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, usersVueDto, usersVueDto.getId());
        return usersVueDto;
    }

    public List<UsersVueDto> readAllEntities() {
        return usersVueMapper.toDtos(usersRepository.findAll());
    }

    @Transactional
    public UsersVueDto updateUsersPartiOne(UpdateUsersPartiOneDto dto) {
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(dto.getUuid())
                .ifPresentOrElse(
                        value -> {
                            if (StringUtils.isNotBlank(dto.getFirstName()))
                                value.setFirstName(dto.getFirstName());

                            if (StringUtils.isNotBlank(dto.getLastName()))
                                value.setLastName(dto.getLastName());

                            if (dto.getBirthDay() != null)
                                value.setBirthdays(dto.getBirthDay());

                            if (StringUtils.isNotBlank(dto.getBusinessNumber()))
                                value.setBusinessNumber(dto.getBusinessNumber());

                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, usersVueDtoAtomicReference.get(), dto.getUuid())
                );
        return usersVueDtoAtomicReference.get();
    }
}
