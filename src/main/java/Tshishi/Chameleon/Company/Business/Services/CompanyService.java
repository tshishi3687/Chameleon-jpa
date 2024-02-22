package Tshishi.Chameleon.Company.Business.Services;

import Tshishi.Chameleon.Company.Business.Dtos.CompanyVueDto;
import Tshishi.Chameleon.Company.Business.Dtos.CreatedCompanyDto;
import Tshishi.Chameleon.Company.Business.Mappers.CompanyVueMapper;
import Tshishi.Chameleon.Company.Business.Mappers.CreatedCompanyMapper;
import Tshishi.Chameleon.Company.DataAccess.Entities.Company;
import Tshishi.Chameleon.Company.DataAccess.Repository.CompanyRepository;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Enum.UsersRoles;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerStep;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.LoggerTypes;
import Tshishi.Chameleon.HumanResources.Business.Services.Common.Logger.ServiceLogs;
import Tshishi.Chameleon.HumanResources.Business.Services.ContactDetailsService;
import Tshishi.Chameleon.HumanResources.Business.Services.UsersService;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.ContactDetailsRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.UsersRepository;
import Tshishi.Chameleon.Securities.Config.JwtAuthenticationFilter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final CreatedCompanyMapper createdCompanyMapper = new CreatedCompanyMapper();
    private final CompanyVueMapper companyVueMapper = new CompanyVueMapper();
    private final ContactDetailsService contactDetailsService;
    private final UsersService usersService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ServiceLogs serviceLogs;

    public CompanyService(CompanyRepository companyRepository, UsersRepository usersRepository, ContactDetailsRepository contactDetailsRepository, ContactDetailsService contactDetailsService, UsersService usersService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.companyRepository = companyRepository;
        this.usersRepository = usersRepository;
        this.contactDetailsRepository = contactDetailsRepository;
        this.contactDetailsService = contactDetailsService;
        this.usersService = usersService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.serviceLogs = new ServiceLogs();
    }

    @Transactional
    public CompanyVueDto addEntity(CreatedCompanyDto dto) {
        if (isNameExist(dto.getName())) {
            serviceLogs.logsConstruction(LoggerStep.EXISTED, LoggerTypes.ADDING_ENTITY, dto, null);
        }
        serviceLogs.logsConstruction(LoggerStep.TRY, LoggerTypes.ADDING_ENTITY, dto, null);
        AtomicReference<CompanyVueDto> companyVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findById(dto.getUsers().getId())
                .ifPresentOrElse(
                        value -> {
                            Company company = createdCompanyMapper.toEntity(dto);
                            ContactDetails contactDetails = contactDetailsRepository.findById(contactDetailsService.addEntity(dto.getContactDetails()).getId()).orElseThrow();
                            company.setContactDetails(contactDetails);
                            company.setTutors(value);
                            companyVueDtoAtomicReference.set(companyVueMapper.toDto(companyRepository.saveAndFlush(company)));
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto, dto.getUsers().getId())
                );
        serviceLogs.logsConstruction(LoggerStep.SUCCESS, LoggerTypes.ADDING_ENTITY, companyVueDtoAtomicReference.get(), companyVueDtoAtomicReference.get().getId());
        return companyVueDtoAtomicReference.get();
    }

    @Transactional
    public CompanyVueDto addNewWorker(UUID companyId, UpdateOrCreateUsers dto) {
        AtomicReference<CompanyVueDto> companyVueDtoAtomicReference = new AtomicReference<>();
        usersRepository.findUsersByMailOrPhoneOrBusinessNumber(jwtAuthenticationFilter.userEmail, jwtAuthenticationFilter.userEmail, jwtAuthenticationFilter.userEmail)
                .ifPresentOrElse(
                        users -> {
                            Roles roles = users.getRolesList().stream().filter(role -> role.getName().equals(UsersRoles.SUPER_ADMIN.getRoleName()))
                                    .findFirst().orElse(null);

                            if (roles == null) {
                                throw new RuntimeException();
                            }
                            companyRepository.findById(companyId)
                                    .ifPresentOrElse(
                                            company -> {
                                                UsersVueDto usersVueDto = usersService.addEntity(dto);
                                                company.getWorkers().add(usersRepository.findById(usersVueDto.getId()).orElseThrow());
                                            },
                                            () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, new CompanyVueDto(), companyId)
                                    );
                        },
                        () -> serviceLogs.logsConstruction(LoggerStep.ERROR, LoggerTypes.READING_ENTITY, dto, companyId)
                );
        return companyVueDtoAtomicReference.get();
    }

    public Boolean isNameExist(String name) {
        return companyRepository.findByName(name).isPresent();
    }
}
