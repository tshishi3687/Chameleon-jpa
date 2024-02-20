package Tshishi.Chameleon.Common.AbstractClass;

import Tshishi.Chameleon.Common.Interface.IdentifedController;
import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/{companyUuid}")
public abstract class BaseController<DTO extends IdentifiedDto<UUID>, UUID> implements IdentifedController<DTO, UUID> {

    protected final IdentifiedService<DTO, UUID> service;

    public BaseController(IdentifiedService<DTO, UUID> service) {
        this.service = service;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO to create", required = true)
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "409", description = "Bad Request", content = @Content)
    public ResponseEntity<DTO> addEntity(@RequestBody @Validated DTO dto, HttpServletRequest request) throws URISyntaxException {
        DTO createdDto = service.addEntity(dto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(String.format("%s/%s", requestUrl, createdDto.getId()));
        return ResponseEntity.created(location).body(createdDto);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    public ResponseEntity<DTO> readEntity(@PathVariable UUID id) {
        return ResponseEntity.ok(service.readEntity(id));
    }


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<DTO>> readAllEntities() {
        List<DTO> list = service.readAllEntities();
        return ResponseEntity.ok(list);
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO to update", required = true)
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    public ResponseEntity<DTO> updateEntity(@RequestBody DTO dto, @PathVariable UUID id) {
        return ResponseEntity.ok(service.updateEntity(dto, id));
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID id) {
        try {
            service.deleteEntity(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
