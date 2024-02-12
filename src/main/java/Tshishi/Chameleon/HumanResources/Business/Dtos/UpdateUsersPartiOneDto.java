package Tshishi.Chameleon.HumanResources.Business.Dtos;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUsersPartiOneDto {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private LocalDate birthDay;
    @Nonnull
    private UUID uuid;
}
