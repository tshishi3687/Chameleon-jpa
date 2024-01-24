package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Roles")
@Tag(name = "Roles Controller", description = "API for managing roles")
public class RolesController extends BaseController<RolesDto, UUID> {

    HttpServletRequest httpServletRequest;
    @Autowired
    public RolesController(IdentifiedService<RolesDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.httpServletRequest = httpServletRequest;
    }
}

