package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.*;
import Tshishi.Chameleon.HumanResources.Business.Mappers.*;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Enum.UsersRoles;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.*;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UsersService implements IdentifiedService<UsersDto, UUID> {

    private final UsersMapper usersMapper = new UsersMapper();
    private final UsersRepository usersRepository;
    private final ServiceLogs serviceLogs;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper = new RolesMapper();
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

    @Override
    @Transactional
    public UsersDto addEntity(UsersDto usersDto) {
        AtomicReference<Users> usersAtomicReference = new AtomicReference<>();
        usersRepository.findUsersByMailOrPhone(usersDto.getMail(), usersDto.getPhone())
                .ifPresentOrElse(
                        value -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, usersDto, value.getId()),
                        () -> {
                            List<Roles> rolesList = rolesRepository.findAll();

                            usersAtomicReference.set(usersMapper.toEntity(usersDto));

                            // Check roles
                            serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, usersDto.getRolesDtoList().stream().findFirst().orElseThrow(), null);
                            if (rolesList.isEmpty()) {
                                // First user using app
                                Roles roles = new Roles();
                                roles.setName(UsersRoles.SUPER_ADMIN.getRoleName());
                                usersAtomicReference.get().getRolesList().add(rolesRepository.save(roles));
                                serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, new RolesDto(), usersAtomicReference.get().getRolesList().stream().findFirst().orElseThrow().getId());
                            } else {
                                // Check if rolesDto not existed
                                usersDto.getRolesDtoList().forEach(rolesDto -> {
                                    if (!rolesRepository.existsById(rolesDto.getId())) {
                                        serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, rolesDto, rolesDto.getId());
                                    }
                                });
                            }

                            // Crypte password
                            usersAtomicReference.get().setPassWord(bCryptPasswordEncoder.encode(usersDto.getPassword()));

                            // Add roles
                            usersAtomicReference.get().getRolesList().addAll(
                                    rolesRepository.findAllByIdIn(
                                            usersDto.getRolesDtoList().stream()
                                                    .filter(rolesDto -> !rolesDto.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName()))
                                                    .map(RolesDto::getId).toList()
                                    )
                            );

                            // creat success logs
                            usersAtomicReference.get().getRolesList().forEach( roles -> serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.READING_ENTITY, new RolesDto(), roles.getId()));

                            // add contact details
                            List<ContactDetails> contactDetailsList = new ArrayList<>();
                            usersDto.getContactDetails().forEach(
                                    contactDetailsDto -> {
                                        // mapping contact detail
                                        ContactDetails contactDetails = contactDetailsMapper.toEntity(contactDetailsDto);

                                        // set locality
                                        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, contactDetailsDto.getLocality(), contactDetailsDto.getLocality().getId());
                                        contactDetails.setLocality(localityRepository.findById(contactDetailsDto.getLocality().getId())
                                                .orElse(localityRepository.save(new Locality(contactDetailsDto.getLocality().getName())))
                                        );

                                        // set Country
                                        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, contactDetailsDto.getCountry(), contactDetailsDto.getCountry().getId());
                                        contactDetails.setCountry(countryRepository.findById(contactDetailsDto.getCountry().getId())
                                                .orElse(countryRepository.save(new Country(contactDetailsDto.getCountry().getName())))
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
        UsersDto usersDto1 = usersMapper.toDto(usersRepository.save(usersAtomicReference.get()));
        usersDto1.setRolesDtoList(rolesMapper.toDtos(usersAtomicReference.get().getRolesList()));
        usersDto1.setContactDetails(contactDetailsMapper.toDtos(usersAtomicReference.get().getContactDetails()));
        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, usersDto1, usersDto1.getId());
        return usersDto1;
    }

    @Override
    public UsersDto readEntity(UUID uuid) {
        return null;
    }

    @Override
    public List<UsersDto> readAllEntities() {
        return null;
    }

    @Override
    public UsersDto updateEntity(UsersDto usersDto, UUID uuid) {
        return null;
    }

    @Override
    public void deleteEntity(UUID uuid) {

    }

    public UsersDto readEntityByMailOrPhone(GetUsersByMailOrPhone getUsersByMailOrPhone) {
        return null;
    }
}
