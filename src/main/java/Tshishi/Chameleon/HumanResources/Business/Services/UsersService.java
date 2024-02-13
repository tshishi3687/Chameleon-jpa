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
import java.util.UUID;
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
    public UsersVueDto updateEntity(UsersCreatedDto dto, UUID usersUuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.UPDATING_ENTITY, dto, usersUuid);
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(usersUuid)
                .ifPresentOrElse(
                        value -> {
                            // Update String elements
                            updateStringElements(value, dto);

                            // Update BirthDay
                            updateBirthday(value, dto);

                            // Update Roles
                            updateRoles(value, dto.getRolesDtoList());

                            // Update Contact Details
                            updateContactDetails(value, dto.getContactDetails());

                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, usersVueDtoAtomicReference.get(),usersUuid);
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, usersVueDtoAtomicReference.get(), usersUuid)
                );
        return usersVueDtoAtomicReference.get();
    }

    private void updateStringElements(Users value, UsersCreatedDto dto) {
        if (StringUtils.isNotBlank(dto.getFirstName())) {
            value.setFirstName(dto.getFirstName());
        }
        if (StringUtils.isNotBlank(dto.getLastName())) {
            value.setLastName(dto.getLastName());
        }
        if (StringUtils.isNotBlank(dto.getMail())) {
            usersRepository.findUsersByMail(dto.getMail())
                    .ifPresentOrElse(
                            usersMailValue -> {
                                if (!value.getId().equals(usersMailValue.getId())) {
                                    serviceLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.ADDING_ENTITY, usersVueMapper.toDto(usersMailValue), usersMailValue.getId());
                                }
                            },
                            () -> value.setMail(dto.getMail())
                    );
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            usersRepository.findUsersByPhone(dto.getPhone())
                    .ifPresentOrElse(
                            usersPhoneValue -> {
                                if (!value.getId().equals(usersPhoneValue.getId())) {
                                    serviceLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.ADDING_ENTITY, usersVueMapper.toDto(usersPhoneValue), usersPhoneValue.getId());
                                }
                            },
                            () -> value.setPhone(dto.getPhone())
                    );
        }
        if (StringUtils.isNotBlank(dto.getBusinessNumber())) {
            usersRepository.findUsersByBusinessNumber(dto.getBusinessNumber())
                    .ifPresentOrElse(
                            usersBusinessValue -> {
                                if (!value.getId().equals(usersBusinessValue.getId())) {
                                    serviceLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.ADDING_ENTITY, usersVueMapper.toDto(usersBusinessValue), usersBusinessValue.getId());
                                }
                            },
                            () -> value.setBusinessNumber(dto.getBusinessNumber())
                    );
        }
        if (StringUtils.isNotBlank(dto.getPassword())) {
            value.setPassWord(dto.getPassword());
        }
    }

    private void updateBirthday(Users value, UsersCreatedDto dto) {
        if (dto.getBirthDay() != null) {
            value.setBirthdays(dto.getBirthDay());
        }
    }

    private void updateRoles(Users value, List<RolesDto> rolesDtoList) {
        value.getRolesList().removeIf(roles -> !roles.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName()));

        rolesDtoList.forEach(rolesDto -> {
            if (!rolesRepository.existsById(rolesDto.getId())) {
                serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, rolesDto, rolesDto.getId());
            } else if (!rolesDto.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName())) {
                value.getRolesList().add(rolesRepository.findById(rolesDto.getId()).orElseThrow());
            }
        });
    }

    private void updateContactDetails(Users value, List<ContactDetailsDto> contactDetailsDtoList) {
        contactDetailsDtoList.forEach(contactDetailsDto -> {
            Country country = getOrCreateCountry(contactDetailsDto.getCountry().getName());
            Locality locality = getOrCreateLocality(contactDetailsDto.getLocality().getName());

            updateOrCreateContactDetails(value, contactDetailsDto, country, locality);
        });
    }

    private Country getOrCreateCountry(String countryName) {
        return countryRepository.findCountryByName(countryName.toUpperCase())
                .orElseGet(() -> countryRepository.saveAndFlush(new Country(countryName.toUpperCase())));
    }

    private Locality getOrCreateLocality(String localityName) {
        return localityRepository.findLocalityByName(localityName.toUpperCase())
                .orElseGet(() -> localityRepository.saveAndFlush(new Locality(localityName.toUpperCase())));
    }

    private void updateOrCreateContactDetails(Users value, ContactDetailsDto contactDetailsDto, Country country, Locality locality) {
        value.getContactDetails().stream()
                .filter(contactDetails1 -> contactDetails1.getId().equals(contactDetailsDto.getId()))
                .findFirst()
                .ifPresentOrElse(contactDetails1 -> {
                            contactDetails1.setAddress(contactDetailsDto.getAddress());
                            contactDetails1.setNumber(contactDetailsDto.getNumber());
                            contactDetails1.setLocality(locality);
                            contactDetails1.setCountry(country);
                        },
                        () -> {
                            ContactDetails contactDetails = contactDetailsRepository.saveAndFlush(new ContactDetails(
                                    contactDetailsDto.getAddress(),
                                    contactDetailsDto.getNumber(),
                                    locality,
                                    country,
                                    value
                            ));
                            value.getContactDetails().add(contactDetails);
                        });
    }

}
