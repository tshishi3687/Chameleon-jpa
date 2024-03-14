package Tshishi.Chameleon.Company.Business.Dtos;

import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCompanyAndUsers {
    private CreatedCompanyDto company;
    private UpdateOrCreateUsers users;
}
