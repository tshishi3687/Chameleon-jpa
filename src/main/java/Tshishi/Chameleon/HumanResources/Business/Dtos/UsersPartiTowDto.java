package Tshishi.Chameleon.HumanResources.Business.Dtos;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
public class UsersPartiTowDto {

    @Nullable
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Adresse e-mail invalide")
    private String mail;
    @Nullable
    @Pattern(regexp = "^(\\+|00)\\d{1,4}[\\s./0-9]*$", message = "Numéro de téléphone européen invalide")
    private String phone;
    @Nullable
    @Pattern(regexp = "^[0-9A-Za-z]{8,}$", message = "Numéro d'entreprise européen invalide")
    private String businessNumber;
    @Nullable
    private String password;
    @Nonnull
    private UUID usersUuid;
}
