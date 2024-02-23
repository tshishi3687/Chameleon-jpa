package Tshishi.Chameleon.Company.Business.Dtos;

import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySelectedDto {

    private CompanyVueDto companyVue;
    private UsersVueDto usersVue;
}
