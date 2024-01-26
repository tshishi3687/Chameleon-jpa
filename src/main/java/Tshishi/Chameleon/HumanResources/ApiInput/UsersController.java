package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Users")
@Tag(name = "Users Controller", description = "API for managing users")
public class UsersController extends BaseController<UsersDto, UUID> {

    HttpServletRequest httpServletRequest;

    @Autowired
    public UsersController(IdentifiedService<UsersDto, UUID> service, HttpServletRequest httpServletRequest){
        super(service);
        this.httpServletRequest = httpServletRequest;
    }
}
