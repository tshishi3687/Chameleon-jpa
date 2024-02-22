package Tshishi.Chameleon.Company.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCompanyDto implements IdentifiedDto<UUID> {

    private UUID id;
    private String name;
    private ContactDetailsDto contactDetails;
    private UsersVueDto users;
}
