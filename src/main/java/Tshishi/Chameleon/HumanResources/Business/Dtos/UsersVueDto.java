package Tshishi.Chameleon.HumanResources.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersVueDto  implements IdentifiedDto<UUID> {

    private UUID id;

    private String initialName;

    private String businessNumber;

    private String mail;

    private String phone;

    private List<RolesDto> rolesDtoList;

    private List<ContactDetailsDto> contactDetails;

    private boolean active;

    @Override
    public String getName() {
        return initialName;
    }
}
