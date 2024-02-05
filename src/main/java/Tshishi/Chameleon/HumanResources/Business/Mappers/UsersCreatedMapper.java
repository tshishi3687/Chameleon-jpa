package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersCreatedDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersCreatedMapper implements IdentifiedMapper<UsersCreatedDto, Users> {

    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();

    @Override
    public UsersCreatedDto toDto(Users users) {
        return new UsersCreatedDto(
                users.getId(),
                users.getFirstName(),
                users.getLastName(),
                users.getBirthdays(),
                users.getMail(),
                users.getPhone(),
                null,
                rolesMapper.toDtos(users.getRolesList()),
                contactDetailsMapper.toDtos(users.getContactDetails())
        );
    }

    @Override
    public Users toEntity(UsersCreatedDto usersCreatedDto) {
        return new Users(
                UUID.randomUUID(),
                usersCreatedDto.getFirstName(),
                usersCreatedDto.getLastName(),
                usersCreatedDto.getMail(),
                usersCreatedDto.getPhone(),
                null,
                usersCreatedDto.getBirthDay(),
                new ArrayList<>(),
                new ArrayList<>()

        );
    }

    @Override
    public List<UsersCreatedDto> toDtos(List<Users> users) {
        return IdentifiedMapper.super.toDtos(users);
    }
}
