package Tshishi.Chameleon.HumanResources.Business.Services;

import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.Business.Mappers.LocalityMapper;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.LocalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class LocalityService implements IdentifiedService<LocalityDto, UUID> {

    private final static Logger logger = Logger.getLogger(LocalityService.class.getSimpleName());
    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper = new LocalityMapper();
    private String errorMessage;

    public LocalityService(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    @Override
    public LocalityDto addEntity(LocalityDto dto) {
//        new ServiceStarterLogs<>(logger, dto).ifAddingEntity();
        localityRepository.findLocalityByName(dto.getName())
                .ifPresentOrElse(
                        value -> {
                            dto.setId(value.getId());
                            dto.setName(value.getName());
                            logger.info(String.format("This locality : \"%s\" existed.", dto.getName()));
                        },
                        () -> {
                            Locality locality = localityRepository.save(localityMapper.toEntity(dto));
                            dto.setId(locality.getId());
                            dto.setName(locality.getName());
                            logger.info(String.format("The locality \"%s\" was successfully registered.", dto.getName()));
                        }
                );
        return dto;
    }

    @Override
    public LocalityDto readEntity(UUID uuid) {
        LocalityDto localityDto = new LocalityDto();
//        new ServiceStarterLogs<>(logger, localityDto).ifReadingEntity(uuid);
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
//        new ServiceStarterLogs<>(logger, new LocalityDto()).ifReadingAllEntity();
        return localityMapper.toDtos(localityRepository.findAll());
    }

    @Override
    public LocalityDto updateEntity(LocalityDto localityDto, UUID uuid) {
//        new ServiceStarterLogs<>(logger, localityDto).ifUpdatingEntity(uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            logger.info(String.format("Locality with id : %s was found", uuid));
                            value.setName(localityDto.getName().toUpperCase());
                            localityRepository.save(value);
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
//        new ServiceStarterLogs<>(logger, new LocalityDto()).ifDeletingEntity(uuid);
        localityRepository.findById(uuid)
                .ifPresentOrElse(
                        value -> {
                            localityRepository.delete(value);
                            logger.info(String.format("This locality with id : %s was found and deleted.", uuid));
                        },
                        () -> {
                            errorMessage = String.format("This locality with id : %s was not found. DELETE FAIL!", uuid);
                            logger.warning(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                );
    }
}
