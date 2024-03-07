package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import Tshishi.Chameleon.HumanResources.Business.Services.LocalityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v01/Locality")
@Tag(name = "Locality Controller", description = "API for managing locality")
public class LocalityController extends BaseController<LocalityDto, UUID> {

    HttpServletRequest httpServletRequest;
    private final LocalityService localityService;

    @Autowired
    public LocalityController(IdentifiedService<LocalityDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.localityService = (LocalityService) service;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("/whichContains/{search}")
    public ResponseEntity<List<LocalityDto>> readAllEntitiesWhichContainsString(@PathVariable String search) {
        return ResponseEntity.ok(localityService.readAllEntitiesWhichContainsString(search));
    }
}
