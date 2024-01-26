package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.*;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceStarterLogs;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsersService implements IdentifiedService<UsersDto, UUID> {

    private final UsersRepository usersRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final RolesRepository rolesRepository;
    private final UsersMapper usersMapper = new UsersMapper();
    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper;
    private final ServiceStarterLogs serviceStarterLogs;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, ContactDetailsRepository contactDetailsRepository, LocalityRepository localityRepository, CountryRepository countryRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.contactDetailsRepository = contactDetailsRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.serviceStarterLogs = new ServiceStarterLogs();
        this.contactDetailsMapper = new ContactDetailsMapper(countryRepository, localityRepository);
    }

    @Override
    public UsersDto addEntity(UsersDto dto) {
        serviceStarterLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        List<ContactDetails> contactDetails = contactDetailsRepository.findAllByMail(dto.getContactDetails().stream().findFirst().orElseThrow().getMail());

        if (!contactDetails.isEmpty())
            serviceStarterLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.ADDING_ENTITY, dto, contactDetails.stream().findFirst().get().getUsers().getId());

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
        UsersDto usersDto = usersMapper.toDto(usersRepository.save(entitySaved));
        serviceStarterLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, usersDto, usersDto.getId());
        return usersDto;
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
}
