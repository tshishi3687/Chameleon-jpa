package Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger;

import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import lombok.NoArgsConstructor;

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
                case READING_ALL_ENTITY -> logger.warning(String.format("Get all %s", dto.getClass().getSimpleName()));
                case UPDATING_ENTITY -> logger.warning(String.format("Try to update %s with id : %s.", dto.getClass().getSimpleName(), uuid));
                case DELETING_ENTITY -> logger.warning(String.format("Try to delete %s with id : %s.", dto.getClass().getSimpleName(), uuid));
            }
        }

        if (loggerStep.equals(LoggerStep.SUCCESS)) {
            switch (loggerTypes) {
                case ADDING_ENTITY -> logger.info(String.format("This %s : \"%s\" existed with id : %s.", dto.getClass().getSimpleName(), dto.getName(), uuid));
                case READING_ENTITY -> logger.info(String.format("This %s with id : %s was found and sent", dto.getClass().getSimpleName(), uuid));
                case UPDATING_ENTITY -> logger.info(String.format("This %s with id : %s was found", dto.getClass().getSimpleName(), uuid));
                case DELETING_ENTITY -> logger.info(String.format("This %s with id : %s was found and deleted.", dto.getClass().getSimpleName(), uuid));
            }
        }

        if (loggerStep.equals(LoggerStep.EXISTED)) {
            switch (loggerTypes) {
                case ADDING_ENTITY -> logger.warning(String.format("%s : \"%s\" existed with id : %s.", dto.getClass().getSimpleName(), dto.getName(), uuid));
                case READING_ENTITY, UPDATING_ENTITY, DELETING_ENTITY -> logger.info(""); // implement later
            }
        }

        if (loggerStep.equals(LoggerStep.ERROR)) {
            String error = "";
            switch (loggerTypes) {
                case ADDING_ENTITY -> error = String.format("This %s : \"%s\" existed with id : %s. CAN'T EXIST TWICE", dto.getClass().getSimpleName(), dto.getName(), uuid);
                case READING_ENTITY -> error = String.format("This %s with id : %s NOT FOUND!", "This roles", uuid);
                case UPDATING_ENTITY -> error = String.format("This %s with id : %s was not found. UPDATE FAIL!", dto.getClass().getSimpleName(), uuid);
                case READING_ALL_ENTITY, DELETING_ENTITY -> error = ""; // implement later
            }
            logger.severe(error);
            throw new RuntimeException(error);
        }
    }
}
