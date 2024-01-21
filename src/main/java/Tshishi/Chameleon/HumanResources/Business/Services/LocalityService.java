package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.LocalityMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class LocalityService implements IdentifiedService<LocalityDto, UUID> {

    private final static Logger logger = Logger.getLogger(LocalityService.class.getName());
    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper = new LocalityMapper();
    private String errorMessage;

    @Override
    public LocalityDto addEntity(LocalityDto localityDto) {
        logger.info(String.format("Try to add %s with name %s.", Locality.class.getName(), localityDto.getName()));

        if (StringUtils.isBlank(localityDto.getName())) {
            errorMessage = "Locality name can't be null!";
            logger.warning(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        localityRepository.findLocalityByName(localityDto.getName())
                .ifPresentOrElse(
                        value -> {
                            localityDto.setId(value.getId());
                            localityDto.setName(value.getName());
                            logger.info(String.format("This locality : \"%s\" existed.", localityDto.getName()));
                        },
                        () -> {
                            Locality locality = localityRepository.save(localityMapper.toEntity(localityDto));
                            localityDto.setId(locality.getId());
                            localityDto.setName(locality.getName());
                            logger.info(String.format("The locality \"%s\" was successfully registered.", localityDto.getName()));
                        }
                );
        return localityDto;
    }

    @Override
    public LocalityDto readEntity(UUID uuid) {
        logger.info(String.format("Try to get locality with id : %s", uuid));

        LocalityDto localityDto = new LocalityDto();
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            localityDto.setId(value.getId());
                            localityDto.setName(value.getName());
                            logger.info(String.format("This locality with id : %s was found and sent.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This locality with id : %s NOT FOUND!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return localityDto;
    }

    @Override
    public List<LocalityDto> readAllEntities() {
        logger.info("Get all locality");
        return localityMapper.toDtos(localityRepository.findAll());
    }

    @Override
    public LocalityDto updateEntity(LocalityDto localityDto, UUID uuid) {
        logger.info(String.format("Try to update locality with id : %s.", uuid));

        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            logger.info(String.format("Locality with id : %s was found", uuid));
                            localityDto.setId(value.getId());
                            localityDto.setName(value.getName());
                            localityRepository.save(localityMapper.toEntity(localityDto));
                            logger.info(String.format("Locality with id : %s was UPDATED", uuid));
                        },
                        () -> {
                            errorMessage = String.format("Locality with id : %s was not found. UPDATE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
        return localityDto;
    }

    @Override
    public void deleteEntity(UUID uuid) {
        logger.info(String.format("Try to delete locality with id : %s.", uuid));

        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            localityRepository.deleteById(uuid);
                            logger.info(String.format("Locality with id : %s was found and deleted.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("Locality with id : %s was not found. DELETE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
