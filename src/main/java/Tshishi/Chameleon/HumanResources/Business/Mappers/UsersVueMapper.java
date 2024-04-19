package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

import java.util.List;

public class UsersVueMapper implements IdentifiedMapper<UsersVueDto, Users> {

    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();

    @Override
    public UsersVueDto toDto(Users users) {
        return new UsersVueDto(
                users.getId(),
                users.getFirstName().toUpperCase() + " " + users.getLastName().toUpperCase(),
                mask(users.getEmail()),
                mask(users.getPhone()),
                rolesMapper.toDtos(users.getRolesList()),
                contactDetailsMapper.toDtos(users.getContactDetails()),
                users.isActive()
                );
    }

    @Override
    public Users toEntity(UsersVueDto usersVueDto) {
        return null;
    }

    @Override
    public List<UsersVueDto> toDtos(List<Users> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    private String mask(String string) {
        StringBuilder masking = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (i <=1) {
                masking.append(string.charAt(i));
            } else if (string.charAt(i) == '@') {
                masking.append(string.charAt(i));
            } else if (i >= string.length() - 2) {
                masking.append(string.charAt(i));
            } else {
                masking.append('*');
            }
        }
        return masking.toString();
    }
}
