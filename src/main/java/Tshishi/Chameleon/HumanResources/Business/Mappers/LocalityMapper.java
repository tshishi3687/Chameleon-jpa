package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;

import java.util.List;
import java.util.logging.Logger;

public class LocalityMapper implements IdentifiedMapper<LocalityDto, Locality> {

    private final static Logger logger = Logger.getLogger(LocalityMapper.class.getName());
    @Override
    public LocalityDto toDto(Locality locality) {
        logger.info(String.format("Beginning of mapping from Entity class \"%s\" to Dto class \"%s\".", Locality.class.getName(), LocalityDto.class.getName()));

        return new LocalityDto(
                locality.getId(),
                locality.getName()
        );
    }

    @Override
    public Locality toEntity(LocalityDto localityDto) {
        logger.info(String.format("Beginning of mapping from Dto class \"%s\" to Entity class \"%s\".", LocalityDto.class.getName(), Locality.class.getName()));

        return new Locality(
                localityDto.getName()
        );
    }

    @Override
    public List<LocalityDto> toDtos(List<Locality> localities) {
        logger.info(String.format("Beginning of mapping from Entities class \"%s\" to Dtos class \"%s\".", Locality.class.getName(), LocalityDto.class.getName()));

        return IdentifiedMapper.super.toDtos(localities);
    }
}
