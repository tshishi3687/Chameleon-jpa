package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class UsersMapper implements IdentifiedMapper<UsersDto, Users> {

    private final static Logger logger = Logger.getLogger(UsersMapper.class.getName());
    @Override
    public UsersDto toDto(Users users) {
        logger.info(String.format("Beginning of mapping from Entity class \"%s\" to Dto class \"%s\".", Users.class.getName(), UsersDto.class.getName()));

        return new UsersDto(
                users.getId(),
                users.getFirstName(),
                users.getLastName(),
                users.getBirthdays(),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Override
    public Users toEntity(UsersDto usersDto) {
        logger.info(String.format("Beginning of mapping from Dto class \"%s\" to Entity class \"%s\".", UsersDto.class.getName(), Users.class.getName()));

        return new Users(
                UUID.randomUUID(),
                usersDto.getFirstName(),
                usersDto.getLastName(),
                null,
                usersDto.getBirthDay(),
                new ArrayList<>(),
                new ArrayList<>()

        );
    }

    @Override
    public List<UsersDto> toDtos(List<Users> users) {
        logger.info(String.format("Beginning of mapping from Entities class \"%s\" to Dtos class \"%s\".", Users.class.getName(), UsersDto.class.getName()));

        return IdentifiedMapper.super.toDtos(users);
    }
}
