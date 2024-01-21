package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Locality;

import java.util.List;

public class LocalityMapper implements IdentifiedMapper<LocalityDto, Locality> {
    @Override
    public LocalityDto toDto(Locality locality) {
        return new LocalityDto(
                locality.getId(),
                locality.getName()
        );
    }

    @Override
    public Locality toEntity(LocalityDto localityDto) {
        return new Locality(
                localityDto.getName()
        );
    }

    @Override
    public List<LocalityDto> toDtos(List<Locality> localities) {
        return IdentifiedMapper.super.toDtos(localities);
    }
}
