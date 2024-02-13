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
import java.util.UUID;

@RestController
@RequestMapping("/Users")
@Tag(name = "Users Controller", description = "API for managing users")
public class UsersController {

    HttpServletRequest httpServletRequest;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService service, HttpServletRequest httpServletRequest) {
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
    public ResponseEntity<List<UsersVueDto>> getAll() {
        return ResponseEntity.ok(usersService.readAllEntities());
    }

    @PostMapping("/usersPartiOne")
    public ResponseEntity<UsersVueDto> usersPartiOne(@RequestBody UsersPartiOneDto usersPartiOneDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.usersPartiOne(usersPartiOneDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

    @PostMapping("/usersPartiTow")
    public ResponseEntity<UsersVueDto> usersPartiTow(@RequestBody UsersPartiTowDto usersPartiTowDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.usersPartiTow(usersPartiTowDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

    @PostMapping("/usersPartiThree")
    public ResponseEntity<UsersVueDto> usersPartiThree(@RequestBody UsersPartiThreeDto usersPartiThreeDto, HttpServletRequest request) throws URISyntaxException {
        UsersVueDto usersVueDto = usersService.usersPartiThree(usersPartiThreeDto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, usersVueDto.getId()));
        return ResponseEntity.created(location).body(usersVueDto);
    }

    @PutMapping("/updateContactDetail/{usersUuid}")
    public ResponseEntity<UsersVueDto> updateContactDetail(@RequestBody ContactDetailsDto contactDetailsDto, @PathVariable UUID usersUuid) {
        return ResponseEntity.ok(usersService.updateContactDetail(contactDetailsDto, usersUuid));
    }

    @PutMapping("/upgradeContactDetails/{usersUuid}")
    public ResponseEntity<UsersVueDto> upgradeContactDetails(@RequestBody ContactDetailsDto contactDetailsDto, @PathVariable UUID usersUuid) {
        return ResponseEntity.ok(usersService.upgradeContactDetails(contactDetailsDto, usersUuid));
    }

}
