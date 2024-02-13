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
    public UsersVueDto usersPartiOne(UsersPartiOneDto dto) {
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(dto.getUsersUuid())
                .ifPresentOrElse(
                        value -> {
                            if (StringUtils.isNotBlank(dto.getFirstName()))
                                value.setFirstName(dto.getFirstName());

                            if (StringUtils.isNotBlank(dto.getLastName()))
                                value.setLastName(dto.getLastName());

                            if (dto.getBirthDay() != null)
                                value.setBirthdays(dto.getBirthDay());

                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, usersVueDtoAtomicReference.get(), dto.getUsersUuid())
                );
        return usersVueDtoAtomicReference.get();
    }

    @Transactional
    public UsersVueDto usersPartiTow(UsersPartiTowDto dto) {
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(dto.getUsersUuid())
                .ifPresentOrElse(
                        value -> {
                            serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.UPDATING_ENTITY, usersVueMapper.toDto(value), value.getId());
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

                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, usersVueMapper.toDto(value), value.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, new UsersVueDto(), dto.getUsersUuid())
                );
        return usersVueDtoAtomicReference.get();
    }

    @Transactional
    public UsersVueDto usersPartiThree(UsersPartiThreeDto dto) {
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(dto.getUsersUuid())
                .ifPresentOrElse(
                        value -> {
                            value.getRolesList().removeIf(roles -> !roles.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName()));

                            dto.getRolesDtos().forEach(rolesDto -> {
                                if (!rolesRepository.existsById(rolesDto.getId())) {
                                    serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, rolesDto, rolesDto.getId());
                                }
                            });

                            dto.getRolesDtos().forEach(rolesDto -> {
                                if (!rolesDto.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName())) {
                                    value.getRolesList().add(rolesRepository.findById(rolesDto.getId())
                                            .orElseThrow());
                                }
                            });
                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.UPDATING_ENTITY, usersVueMapper.toDto(value), value.getId());
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, new UsersVueDto(), dto.getUsersUuid())
                );
        return usersVueDtoAtomicReference.get();
    }

    @Transactional
    public UsersVueDto updateContactDetail(ContactDetailsDto dto, UUID usersUuid) {
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(usersUuid)
                .ifPresentOrElse(
                        value -> value.getContactDetails().stream()
                                .filter(contactDetails -> contactDetails.getId().equals(dto.getId()))
                                .findFirst()
                                .ifPresentOrElse(
                                        contactDetails -> {
                                            contactDetails.setAddress(dto.getAddress());
                                            contactDetails.setNumber(dto.getNumber());
                                            contactDetails.setLocality(localityRepository.findLocalityByName(dto.getLocality().getName())
                                                    .orElse(localityRepository.save(new Locality(dto.getLocality().getName().toUpperCase()))));
                                            contactDetails.setCountry(countryRepository.findCountryByName(dto.getCountry().getName())
                                                    .orElse(countryRepository.save(new Country(dto.getCountry().getName().toUpperCase()))));
                                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                                        },
                                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto, dto.getId())
                                ),
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, new UsersVueDto(), usersUuid)
                );
        return usersVueDtoAtomicReference.get();
    }

    @Transactional
    public UsersVueDto upgradeContactDetails(ContactDetailsDto dto, UUID usersUuid) {
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, usersUuid);
        AtomicReference<UsersVueDto> usersVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(usersUuid)
                .ifPresentOrElse(
                        value -> {
                            Country country = countryRepository.findCountryByName(dto.getCountry().getName().toUpperCase())
                                    .orElseGet(() -> countryRepository.saveAndFlush(new Country(dto.getCountry().getName().toUpperCase())));

                            Locality locality = localityRepository.findLocalityByName(dto.getLocality().getName().toUpperCase())
                                    .orElseGet(() -> localityRepository.saveAndFlush(new Locality(dto.getLocality().getName().toUpperCase())));


                            // Assurez-vous que les entités sont persistées avant de continuer
                            countryRepository.saveAndFlush(country);
                            localityRepository.saveAndFlush(locality);
                            ContactDetails contactDetails = contactDetailsRepository.saveAndFlush(new ContactDetails(
                                    dto.getAddress(),
                                    dto.getNumber(),
                                    locality,
                                    country,
                                    value
                            ));
                            value.getContactDetails().add(contactDetails);

                            usersVueDtoAtomicReference.set(usersVueMapper.toDto(value));
                            serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, dto, usersUuid);
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, new UsersVueDto(), usersUuid)
                );
        return usersVueDtoAtomicReference.get();
    }
}
