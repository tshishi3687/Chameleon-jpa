package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersMapper implements IdentifiedMapper<UsersDto, Users> {

    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();

    @Override
    public UsersDto toDto(Users users) {
        return new UsersDto(
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
    public Users toEntity(UsersDto usersDto) {
        return new Users(
                UUID.randomUUID(),
                usersDto.getFirstName(),
                usersDto.getLastName(),
                usersDto.getMail(),
                usersDto.getPhone(),
                null,
                usersDto.getBirthDay(),
                new ArrayList<>(),
                new ArrayList<>()

        );
    }

    @Override
    public List<UsersDto> toDtos(List<Users> users) {
        return IdentifiedMapper.super.toDtos(users);
    }
}
