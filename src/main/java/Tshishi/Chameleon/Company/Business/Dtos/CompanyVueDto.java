package Tshishi.Chameleon.Company.Business.Dtos;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyVueDto implements IdentifiedDto<UUID> {

    private UUID id;
    private String name;
    private String businessNumber;
    private SubscriptionDto subscription;
}
