package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersCreatedDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersCreatedMapper implements IdentifiedMapper<UsersCreatedDto, Users> {

    @Override
    public UsersCreatedDto toDto(Users users) {
        return null;
    }

    @Override
    public Users toEntity(UsersCreatedDto usersCreatedDto) {
        return new Users(
                UUID.randomUUID(),
                usersCreatedDto.getFirstName(),
                usersCreatedDto.getLastName(),
                usersCreatedDto.getBusinessNumber(),
                usersCreatedDto.getMail(),
                usersCreatedDto.getPhone(),
                null,
                usersCreatedDto.getBirthDay(),
                new ArrayList<>(),
                new ArrayList<>(),
                true
        );
    }

    @Override
    public List<UsersCreatedDto> toDtos(List<Users> users) {
        return IdentifiedMapper.super.toDtos(users);
    }
}
