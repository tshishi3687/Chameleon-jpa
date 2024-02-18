package Tshishi.Chameleon.HumanResources.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrCreateUsers implements IdentifiedDto<UUID> {

    private UUID id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @Pattern(regexp = "^[0-9A-Za-z]{8,}$", message = "Numéro d'entreprise européen invalide")
    private String businessNumber;

    private LocalDate birthDay;

    @NonNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Adresse e-mail invalide")
    private String mail;

    @NonNull
    @Pattern(regexp = "^(\\+|00)\\d{1,4}[\\s./0-9]*$", message = "Numéro de téléphone européen invalide")
    private String phone;

    private String password;

    private List<RolesDto> rolesDtoList;

    private List<ContactDetailsDto> contactDetails;

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
