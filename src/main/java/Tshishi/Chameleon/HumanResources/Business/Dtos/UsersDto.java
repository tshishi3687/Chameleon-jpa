package Tshishi.Chameleon.HumanResources.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto implements IdentifiedDto<UUID> {

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate birthDay;

    private List<RolesDto> rolesDtoList;

    private ContactDetailsDto contactDetails;
}
