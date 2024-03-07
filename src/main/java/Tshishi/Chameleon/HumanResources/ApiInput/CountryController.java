package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import Tshishi.Chameleon.HumanResources.Business.Services.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v01/Country")
@Tag(name = "Country Controller", description = "API for managing country")
public class CountryController extends BaseController<CountryDto, UUID> {

    HttpServletRequest httpServletRequest;
    private final CountryService countryService;

    public CountryController(IdentifiedService<CountryDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.httpServletRequest = httpServletRequest;
        this.countryService = (CountryService) service;
    }

    @GetMapping("/whichContains/{search}")
    public ResponseEntity<List<CountryDto>> readAllEntitiesWhichContainsString(@PathVariable String search) {
        return ResponseEntity.ok(countryService.readAllEntitiesWhichContainsString(search));
    }
}
