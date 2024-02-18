package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersCreatedMapper implements IdentifiedMapper<UpdateOrCreateUsers, Users> {

    @Override
    public UpdateOrCreateUsers toDto(Users users) {
        return null;
    }

    @Override
    public Users toEntity(UpdateOrCreateUsers updateOrCreateUsers) {
        return new Users(
                UUID.randomUUID(),
                updateOrCreateUsers.getFirstName(),
                updateOrCreateUsers.getLastName(),
                updateOrCreateUsers.getBusinessNumber(),
                updateOrCreateUsers.getMail(),
                updateOrCreateUsers.getPhone(),
                null,
                updateOrCreateUsers.getBirthDay(),
                new ArrayList<>(),
                new ArrayList<>(),
                true
        );
    }

    @Override
    public List<UpdateOrCreateUsers> toDtos(List<Users> users) {
        return IdentifiedMapper.super.toDtos(users);
    }
}
