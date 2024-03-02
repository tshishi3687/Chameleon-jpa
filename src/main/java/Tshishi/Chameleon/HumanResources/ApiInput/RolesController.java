package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Services.RolesService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v01/Roles")
@Tag(name = "Roles Controller", description = "API for managing roles")
public class RolesController extends BaseController<RolesDto, UUID> {

    HttpServletRequest httpServletRequest;
    private final RolesService rolesService;

    @Autowired
    public RolesController(IdentifiedService<RolesDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.rolesService = (RolesService) service;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping(value = "/{companyId}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<RolesDto>> readAllEntities(@PathVariable UUID companyId) {
        List<RolesDto> list = rolesService.readAllRoles(companyId);
        return ResponseEntity.ok(list);
    }
}

