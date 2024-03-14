package Tshishi.Chameleon.Company.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.Company.Business.Dtos.CreatedCompanyDto;
import Tshishi.Chameleon.Company.DataAccess.Entities.Company;

import java.util.ArrayList;
import java.util.List;

public class CreatedCompanyMapper implements IdentifiedMapper<CreatedCompanyDto, Company> {

    @Override
    public CreatedCompanyDto toDto(Company company) {
        return null;
    }

    @Override
    public Company toEntity(CreatedCompanyDto createdCompanyDto) {
        return new Company(
                createdCompanyDto.getName(),
                createdCompanyDto.getBusinessNumber(),
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Override
    public List<CreatedCompanyDto> toDtos(List<Company> companies) {
        return IdentifiedMapper.super.toDtos(companies);
    }
}
