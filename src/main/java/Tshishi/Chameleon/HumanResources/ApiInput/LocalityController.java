package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.LocalityDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Locality")
@Tag(name = "Locality Controller", description = "API for managing locality")
public class LocalityController extends BaseController<LocalityDto, UUID> {

    HttpServletRequest httpServletRequest;

    @Autowired
    public LocalityController(IdentifiedService<LocalityDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.httpServletRequest = httpServletRequest;
    }
}
