package Tshishi.Chameleon.Common.AbstractClass;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import io.micrometer.common.util.StringUtils;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

@NoArgsConstructor
public abstract class BaseService<Dto extends IdentifiedDto<UUID>, UUID> implements IdentifiedService<Dto, UUID> {
    protected Logger logger;
    protected String dtoClassName;
    protected String errorMessage;

    public BaseService(Logger logger, String dtoClassName) {
        this.logger = logger;
        this.dtoClassName = dtoClassName;
    }

    @Override
    public Dto addEntity(Dto dto) {
        logger.info(String.format("Try to add %s with name %s.", dtoClassName, dto.getName()));

        if (StringUtils.isBlank(dto.getName())) {
            errorMessage = String.format("%s name can't be null!", dto.getClass().getName());
            logger.warning(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        return null;
    }

    @Override
    public Dto readEntity(UUID uuid) {
        logger.info(String.format("Try to get %s with id : %s.", dtoClassName, uuid));

        return null;
    }

    @Override
    public List<Dto> readAllEntities() {
        logger.info(String.format("Get all %s", dtoClassName));

        return null;
    }

    @Override
    public Dto updateEntity(Dto dto, UUID uuid) {
        logger.info(String.format("Try to update %s with id : %s.", dtoClassName, uuid));

        return null;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        logger.info(String.format("Try to delete %s with id : %s.", dtoClassName, uuid));

    }
}
