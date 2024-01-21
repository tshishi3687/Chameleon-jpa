package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersMapper implements IdentifiedMapper<UsersDto, Users> {
    @Override
    public UsersDto toDto(Users users) {
        return new UsersDto(
                users.getId(),
                users.getFirstName(),
                users.getLastName(),
                users.getBirthdays(),
                new ArrayList<>(),
                null
        );
    }

    @Override
    public Users toEntity(UsersDto usersDto) {
        return new Users(
                UUID.randomUUID(),
                usersDto.getFirstName(),
                usersDto.getLastName(),
                null,
                usersDto.getBirthDay(),
                new ArrayList<>(),
                null

        );
    }

    @Override
    public List<UsersDto> toDtos(List<Users> users) {
        return IdentifiedMapper.super.toDtos(users);
    }
}
