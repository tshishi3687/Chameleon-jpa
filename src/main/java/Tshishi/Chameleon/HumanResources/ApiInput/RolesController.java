package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Roles Controller", description = "API for managing roles")
public class RolesController extends BaseController<RolesDto, UUID> {

    @Autowired
    public RolesController(IdentifiedService<RolesDto, UUID> service) {
        super(service);
    }

    @Override
    public ResponseEntity<RolesDto> addEntity(@Validated @RequestBody RolesDto dto) throws URISyntaxException {
        return super.addEntity(dto, null);
    }
}

