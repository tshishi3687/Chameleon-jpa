package Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.logging.Logger;

@NoArgsConstructor
public class ServiceStarterLogs {

    private static final Logger logger = Logger.getLogger(ServiceStarterLogs.class.getSimpleName());

    public void logsConstruction(LoggerStep loggerStep, LoggerTypes loggerTypes, IdentifiedDto<UUID> dto, UUID uuid) {

        if (loggerStep.equals(LoggerStep.TRY)) {
            switch (loggerTypes) {
                case ADDING_ENTITY -> logger.warning(String.format("Try to add %s with name %s.", dto.getClass().getSimpleName(), uuid));
                case READING_ENTITY -> logger.warning(String.format("Try to get %s with id : %s.", dto.getClass().getSimpleName(), uuid));
                case READIND_ENTITY_By_MAIL_OR_PHONE -> logger.warning(String.format("Try to get %s by mail or phone: \"%s\".", dto.getClass().getSimpleName(), dto.getName()));
                case READING_ALL_ENTITY -> logger.warning(String.format("Get all %s", dto.getClass().getSimpleName()));
                case UPDATING_ENTITY -> logger.warning(String.format("Try to update %s with id : %s.", dto.getClass().getSimpleName(), uuid));
                case DELETING_ENTITY -> logger.warning(String.format("Try to delete %s with id : %s.", dto.getClass().getSimpleName(), uuid));
            }
            if (StringUtils.isBlank(dto.getName()) && !loggerTypes.equals(LoggerTypes.READING_ALL_ENTITY)) {
                String error = String.format("This %s .getName was not be null!", dto.getClass().getSimpleName());
                logger.severe(error);
                throw new RuntimeException(error);
            }
        }

        if (loggerStep.equals(LoggerStep.SUCCESS)) {
            switch (loggerTypes) {
                case ADDING_ENTITY -> logger.info(String.format("This %s : \"%s\" saved with id : %s.", dto.getClass().getSimpleName(), dto.getName(), uuid));
                case READING_ENTITY -> logger.info(String.format("This %s with id : %s was found and sent", dto.getClass().getSimpleName(), uuid));
                case UPDATING_ENTITY -> logger.info(String.format("This %s with id : %s was found", dto.getClass().getSimpleName(), uuid));
                case DELETING_ENTITY -> logger.info(String.format("This %s with id : %s was found and deleted.", dto.getClass().getSimpleName(), uuid));
            }
        }

        if (loggerStep.equals(LoggerStep.EXISTED)) {
            String error = "";
            switch (loggerTypes) {
                case ADDING_ENTITY -> {
                    error = String.format("%s : \"%s\" existed with id : %s. A user can have several contact cards but each contact card must uniquely contain an email and a phone number", dto.getClass().getSimpleName(), dto.getName(), uuid);
                    logger.warning(error);
                }
                case READING_ENTITY, UPDATING_ENTITY, DELETING_ENTITY -> {
                    error = ""; // implement later
                    logger.info(error); // implement later
                }
            }
            throw new RuntimeException(error);
        }

        if (loggerStep.equals(LoggerStep.ERROR)) {
            String error = "";
            switch (loggerTypes) {
                case ADDING_ENTITY -> error = String.format("This %s : \"%s\" existed with id : %s. CAN'T EXIST TWICE", dto.getClass().getSimpleName(), dto.getName(), uuid);
                case READING_ENTITY -> error = String.format("This %s with id : %s NOT FOUND!", "This roles", uuid);
                case READIND_ENTITY_By_MAIL_OR_PHONE -> error = String.format("This %s with name : %s NOT FOUND!", dto.getClass().getSimpleName(), dto.getName());
                case UPDATING_ENTITY -> error = String.format("This %s with id : %s was not found. UPDATE FAIL!", dto.getClass().getSimpleName(), uuid);
                case READING_ALL_ENTITY, DELETING_ENTITY -> error = ""; // implement later
            }
            logger.severe(error);
            throw new RuntimeException(error);
        }
    }
}
