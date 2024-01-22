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

    @NonNull
    private String mail;

    @NonNull
    private String phone;

    @NonNull
    private String address;

    @NonNull
    private String number;

    private LocalityDto locality;

    private CountryDto country;

    private UsersDto users;

    @Override
    public String getName() {
        return null;
    }
}
