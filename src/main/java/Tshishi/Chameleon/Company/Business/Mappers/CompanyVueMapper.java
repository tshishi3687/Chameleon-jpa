package Tshishi.Chameleon.Company.Business.Mappers;

import Tshishi.Chameleon.Common.Interface.IdentifiedMapper;
import Tshishi.Chameleon.Company.Business.Dtos.CompanyVueDto;
import Tshishi.Chameleon.Company.DataAccess.Entities.Company;

import java.util.List;

public class CompanyVueMapper implements IdentifiedMapper<CompanyVueDto, Company> {

    @Override
    public CompanyVueDto toDto(Company company) {
        return new CompanyVueDto(
                company.getId(),
                company.getName()
        );
    }

    @Override
    public Company toEntity(CompanyVueDto companyVueDto) {
        return null;
    }

    @Override
    public List<CompanyVueDto> toDtos(List<Company> companies) {
        return IdentifiedMapper.super.toDtos(companies);
    }
}
