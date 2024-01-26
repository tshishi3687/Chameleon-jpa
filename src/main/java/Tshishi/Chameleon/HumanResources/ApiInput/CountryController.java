package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.CountryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Country")
@Tag(name = "Country Controller", description = "API for managing country")
public class CountryController extends BaseController<CountryDto, UUID> {

    HttpServletRequest httpServletRequest;

    public CountryController(IdentifiedService<CountryDto, UUID> service, HttpServletRequest httpServletRequest) {
        super(service);
        this.httpServletRequest = httpServletRequest;
    }
}
