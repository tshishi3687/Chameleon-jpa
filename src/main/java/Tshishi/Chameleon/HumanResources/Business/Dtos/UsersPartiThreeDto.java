package Tshishi.Chameleon.HumanResources.Business.Dtos;

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
public class UsersPartiThreeDto {

    private List<RolesDto> rolesDtos;
    private UUID usersUuid;
}
