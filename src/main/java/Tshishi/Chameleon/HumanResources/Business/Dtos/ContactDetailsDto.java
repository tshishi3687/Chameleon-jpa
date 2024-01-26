package Tshishi.Chameleon.HumanResources.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetailsDto implements IdentifiedDto<UUID> {

    private UUID id;

    private String mail;

    private String phone;

    private String address;

    private String number;

    private LocalityDto locality;

    private CountryDto country;

    @Override
    public String getName() {
        return null;
    }
}
