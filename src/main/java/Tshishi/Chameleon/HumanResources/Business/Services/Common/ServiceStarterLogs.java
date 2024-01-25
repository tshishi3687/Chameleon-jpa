package Tshishi.Chameleon.HumanResources.Business.Services.Common;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import io.micrometer.common.util.StringUtils;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

@NoArgsConstructor
public class ServiceStarterLogs<DTO extends IdentifiedDto<UUID>, UUID> {

    private Logger logger;
    private DTO dto;

    public ServiceStarterLogs(Logger logger, DTO dto) {
        this.logger = logger;
        this.dto = dto;
    }

    public void ifAddingEntity() {
        logger.warning(String.format("Try to add %s with name %s.", dto.getClass().getSimpleName(), dto.getName()));

        if (StringUtils.isBlank(dto.getName())) {
            String errorMessage = String.format("%s name can't be null!", dto.getClass().getSimpleName());
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void ifReadingEntity(UUID uuid){
       logger.warning(String.format("Try to get %s with id : %s.", dto.getClass().getSimpleName(), uuid));
    }

    public void ifReadingAllEntity(){
        logger.warning(String.format("Get all %s", dto.getClass().getSimpleName()));
    }

    public void ifUpdatingEntity(UUID uuid){
        logger.warning(String.format("Try to update %s with id : %s.", dto.getClass().getSimpleName(), uuid));
    }

    public void ifDeletingEntity(UUID uuid){
        logger.warning(String.format("Try to delete %s with id : %s.", dto.getClass().getSimpleName(), uuid));
    }
}
