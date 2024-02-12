package Tshishi.Chameleon.HumanResources.ApiInput;

import Tshishi.Chameleon.HumanResources.Business.Dtos.*;
import Tshishi.Chameleon.HumanResources.Business.Services.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/Users")
@Tag(name = "Users Controller", description = "API for managing users")
public class UsersController  {

    HttpServletRequest httpServletRequest;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService service, HttpServletRequest httpServletRequest){
        this.usersService = service;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping
    public ResponseEntity<UsersVueDto> addingEntity(@RequestBody @Validated UsersCreatedDto dto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto createdDto = usersService.addEntity(dto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, createdDto.getId()));
        return ResponseEntity.created(location).body(createdDto);
    }

    @GetMapping
    public ResponseEntity<List<UsersVueDto>> getAll(){
        return ResponseEntity.ok(usersService.readAllEntities());
    }

    @PostMapping("/updateUsersPartiOne")
    public ResponseEntity<UsersVueDto> updateUsersPartiOne(@RequestBody UpdateUsersPartiOneDto updateUsersPartiOneDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.updateUsersPartiOne(updateUsersPartiOneDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

    @PostMapping("/updateUsersPartiTow")
    public ResponseEntity<UsersVueDto> updateUsersPartiTow(@RequestBody UpdateUsersPartiTowDto updateUsersPartiTowDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.updateUsersPartiTow(updateUsersPartiTowDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

    @PostMapping("/updateUsersPartiThree")
    public ResponseEntity<UsersVueDto> updateUsersPartiThree(@RequestBody UpdateUsersPartiThreeDto updateUsersPartiThreeDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.updateUsersPartiThree(updateUsersPartiThreeDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

}
