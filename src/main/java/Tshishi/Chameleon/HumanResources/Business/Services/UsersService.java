package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.*;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UsersService implements IdentifiedService<UsersDto, UUID> {

    private final UsersRepository usersRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final RolesRepository rolesRepository;
    private final LocalityRepository localityRepository;
    private final CountryRepository countryRepository;
    private final UsersMapper usersMapper = new UsersMapper();
    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper;
    private final LocalityMapper localityMapper = new LocalityMapper();
    private final CountryMapper countryMapper = new CountryMapper();
    private final ServiceStarterLogs serviceStarterLogs;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, ContactDetailsRepository contactDetailsRepository, LocalityRepository localityRepository1, CountryRepository countryRepository1, LocalityRepository localityRepository, CountryRepository countryRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.contactDetailsRepository = contactDetailsRepository;
        this.localityRepository = localityRepository1;
        this.countryRepository = countryRepository1;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.serviceStarterLogs = new ServiceStarterLogs();
        this.contactDetailsMapper = new ContactDetailsMapper(countryRepository, localityRepository);
    }

    @Override
    public UsersDto addEntity(UsersDto dto) {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        AtomicReference<UsersDto> usersDto = new AtomicReference<>();
        dto.getContactDetails().forEach(contact ->
                contactDetailsRepository.findByMailOrPhone(contact.getMail(), contact.getPhone())
                        .ifPresentOrElse(
                                value -> serviceStarterLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.ADDING_ENTITY, dto, value.getUsers().getId()),
                                () -> {

                                    // Check roles must always exit: if existed -> take good message or -> take bad message
                                    dto.getRolesDtoList().forEach(rolesDto -> rolesRepository.findRolesByName(rolesDto.getName())
                                            .ifPresentOrElse(
                                                    value -> serviceStarterLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.READING_ENTITY, rolesDto, value.getId()),
                                                    () -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, rolesDto, null)
                                            ));

                                    // Check country: if existed -> take it or -> create it
                                    countryRepository.findCountryByName(contact.getCountry().getName())
                                            .ifPresentOrElse(
                                                    value -> {
                                                        contact.getCountry().setId(value.getId());
                                                        contact.getCountry().setName(value.getName());
                                                    },
                                                    () -> contact.setCountry(
                                                            countryMapper.toDto(
                                                                    countryRepository.save(
                                                                            countryMapper.toEntity(
                                                                                    contact.getCountry()
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            );

                                    // Check locality: if existed -> take it or -> create it
                                    localityRepository.findLocalityByName(contact.getLocality().getName())
                                            .ifPresentOrElse(
                                                    value -> {
                                                        contact.getLocality().setId(value.getId());
                                                        contact.getLocality().setName(value.getName());
                                                    },
                                                    () -> contact.setLocality(
                                                            localityMapper.toDto(
                                                                    localityRepository.save(
                                                                            localityMapper.toEntity(
                                                                                    contact.getLocality()
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            );

                                    Users entity = usersMapper.toEntity(dto);
                                    entity.setPassWord(bCryptPasswordEncoder.encode(dto.getPassword()));
                                    entity.getRolesList().addAll(rolesRepository.findAllById(
                                            dto.getRolesDtoList()
                                                    .stream()
                                                    .map(rolesMapper::toEntity)
                                                    .map(Roles::getId)
                                                    .toList()));
                                    Users entitySaved = usersRepository.save(entity);

                                    entitySaved.getContactDetails().addAll(
                                            contactDetailsRepository.saveAll(
                                                    dto.getContactDetails()
                                                            .stream()
                                                            .map(contactDetailsMapper::toEntity)
                                                            .peek(contactDetails1 -> contactDetails1.setUsers(entitySaved))
                                                            .toList()));
                                    usersDto.set(usersMapper.toDto(usersRepository.save(entitySaved)));
                                    serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, usersDto.get(), usersDto.get().getId());
                                }
                        ));


        return usersDto.get();
    }

    @Override
    public UsersDto readEntity(UUID uuid) {
        AtomicReference<UsersDto> dto = new AtomicReference<>();
        serviceStarterLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.READING_ENTITY, new UsersDto(), uuid);
        usersRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            UsersDto usersDto = usersMapper.toDto(value);
                            usersDto.setRolesDtoList(rolesMapper.toDtos(value.getRolesList()));
                            usersDto.setContactDetails(contactDetailsMapper.toDtos(value.getContactDetails()));
                            dto.set(usersDto);
                        },
                        () -> serviceStarterLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto.get(), uuid)
                );
        serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.READING_ENTITY, dto.get(), dto.get().getId());
        return dto.get();
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
}
