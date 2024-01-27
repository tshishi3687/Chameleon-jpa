package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.Common.AbstractClass.BaseController;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import Tshishi.Chameleon.HumanResources.Business.Dtos.GetUsersByMailOrPhone;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersDto;
import Tshishi.Chameleon.HumanResources.Business.Services.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Users")
@Tag(name = "Users Controller", description = "API for managing users")
public class UsersController extends BaseController<UsersDto, UUID> {

    HttpServletRequest httpServletRequest;
    private final UsersService usersService;

    @Autowired
    public UsersController(IdentifiedService<UsersDto, UUID> service, HttpServletRequest httpServletRequest){
        super(service);
        this.usersService = (UsersService) service;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("/mailOrPhone/{nameOrPhone}")
    public ResponseEntity<UsersDto> getUsersByMailOrPhone(@PathVariable String nameOrPhone) {
        GetUsersByMailOrPhone getUsersByMailOrPhone = new GetUsersByMailOrPhone();
        getUsersByMailOrPhone.setNameOrPhone(nameOrPhone);
        return ResponseEntity.ok(usersService.readEntityByMailOrPhone(getUsersByMailOrPhone));
    }
}
