package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;

public class UsersVueMapper {

    private final RolesMapper rolesMapper = new RolesMapper();
    private final ContactDetailsMapper contactDetailsMapper = new ContactDetailsMapper();

    public UsersVueDto toDto(Users users) {
        return new UsersVueDto(
                users.getId(),
                users.getFirstName().toUpperCase().charAt(0) + "." + users.getLastName().toUpperCase().charAt(0),
                mask(users.getMail()),
                mask(users.getPhone()),
                rolesMapper.toDtos(users.getRolesList()),
                contactDetailsMapper.toDtos(users.getContactDetails())
                );
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
