package Tshishi.Chameleon.HumanResources.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
@NoArgsConstructor
public class GetUsersByMailOrPhone implements IdentifiedDto<UUID> {

    private String nameOrPhone;

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public String getName() {
        return nameOrPhone;
    }
}
