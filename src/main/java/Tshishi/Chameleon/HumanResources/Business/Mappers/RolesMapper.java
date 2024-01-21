package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RolesMapper implements IdentifiedMapper<RolesDto, Roles> {

    private final static Logger logger = Logger.getLogger(RolesMapper.class.getName());
    @Override
    public RolesDto toDto(Roles roles) {
        logger.info(String.format("Beginning of mapping from Entity class \"%s\" to Dto class \"%s\".", Roles.class.getName(), RolesDto.class.getName()));

        return new RolesDto(
                roles.getId(),
                roles.getName()
        );
    }

    @Override
    public Roles toEntity(RolesDto rolesDto) {
        logger.info(String.format("Beginning of mapping from Dto class \"%s\" to Entity class \"%s\".", RolesDto.class.getName(), Roles.class.getName()));

        return new Roles(
                rolesDto.getName(),
                new ArrayList<>()
        );
    }

    @Override
    public List<RolesDto> toDtos(List<Roles> roles) {
        logger.info(String.format("Beginning of mapping from Entities class \"%s\" to Dtos class \"%s\".", Roles.class.getName(), RolesDto.class.getName()));

        return IdentifiedMapper.super.toDtos(roles);
    }
}
