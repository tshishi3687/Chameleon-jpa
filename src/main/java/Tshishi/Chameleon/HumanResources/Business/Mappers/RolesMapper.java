package Tshishi.Chameleon.HumanResources.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;

import java.util.ArrayList;
import java.util.List;

public class RolesMapper implements IdentifiedMapper<RolesDto, Roles> {

    @Override
    public RolesDto toDto(Roles roles) {
        return new RolesDto(
                roles.getId(),
                roles.getName()
        );
    }

    @Override
    public Roles toEntity(RolesDto rolesDto) {
        return new Roles(
                rolesDto.getName().toUpperCase(),
                new ArrayList<>()
        );
    }

    @Override
    public List<RolesDto> toDtos(List<Roles> roles) {
        return IdentifiedMapper.super.toDtos(roles);
    }
}
