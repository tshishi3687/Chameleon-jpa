package Tshishi.Chameleon.Company.ApiInput;

import Tshishi.Chameleon.Company.Business.Dtos.CompanySelectedDto;
import Tshishi.Chameleon.Company.Business.Dtos.CompanyVueDto;
import Tshishi.Chameleon.Company.Business.Dtos.CreatedCompanyDto;
import Tshishi.Chameleon.Company.Business.Services.CompanyService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v01/company")
@Tag(name = "Company Controller", description = "API for managing company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/add/{usersUuid}")
    public ResponseEntity<CompanyVueDto> add(@RequestBody CreatedCompanyDto dto, HttpServletRequest request, @PathVariable UUID usersUuid) throws URISyntaxException {
        CompanyVueDto companyVueDto1 = companyService.addEntity(dto, usersUuid);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, companyVueDto1.getId()));
        return ResponseEntity.created(location).body(companyVueDto1);
    }

    @PostMapping("/addNewWorker/{companyId}")
    public ResponseEntity<CompanyVueDto> addNewWorker(@RequestBody UpdateOrCreateUsers dto, @PathVariable UUID companyId, HttpServletRequest request) throws URISyntaxException {
        CompanyVueDto companyVueDto1 = companyService.addNewWorker(companyId, dto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, companyVueDto1.getId()));
        return ResponseEntity.created(location).body(companyVueDto1);
    }

    @GetMapping("/selected/{companyUuid}")
    public ResponseEntity<CompanySelectedDto> getSelectedCompany(@PathVariable UUID companyUuid) {
        return ResponseEntity.ok(companyService.getSelectedCompany(companyUuid));
    }

    @GetMapping("/mines/{usersUuid}")
    public ResponseEntity<List<CompanyVueDto>> getCompany(@PathVariable UUID usersUuid) {
        return ResponseEntity.ok(companyService.getAllMineCompanies(usersUuid));
    }
}
