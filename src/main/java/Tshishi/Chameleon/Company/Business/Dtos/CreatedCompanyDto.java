package Tshishi.Chameleon.Company.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.ContactDetailsDto;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^[0-9A-Za-z]{8,}$", message = "Numéro d'entreprise européen invalide")
    private String businessNumber;

    private ContactDetailsDto contactDetails;
}
