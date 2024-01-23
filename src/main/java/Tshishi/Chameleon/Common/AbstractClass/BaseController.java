package Tshishi.Chameleon.Common.AbstractClass;

import Tshishi.Chameleon.Common.Interface.IdentifedController;
import Tshishi.Chameleon.Common.Interface.IdentifiedDto;
import Tshishi.Chameleon.Common.Interface.IdentifiedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public abstract class BaseController<DTO extends IdentifiedDto<UUID>, UUID> implements IdentifedController<DTO, UUID> {

    protected final IdentifiedService<DTO, UUID> service;

    public BaseController(IdentifiedService<DTO, UUID> service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DTO> addEntity(@RequestBody @Validated DTO dto, HttpServletRequest request) throws URISyntaxException {
        DTO createdDto = service.addEntity(dto);
        String requestUrl = request.getRequestURL().toString();
        URI location = new URI(requestUrl + "/" + createdDto.getId());
        return ResponseEntity.created(location).body(createdDto);
    }

    // READ_ONE - GET > http://localhost:8081/?/{id}
    @GetMapping("/{uuid}")
    public ResponseEntity<DTO> readEntity(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.readEntity(uuid));
    }


    // READ_ALL - GET > http://localhost:8081/?
    @GetMapping
    public ResponseEntity<List<DTO>> readAllEntities() {
        List<DTO> list = service.readAllEntities();
        return ResponseEntity.ok(list);
    }

    // UPDATE - PUT > http://localhost:8081/?
    @PutMapping("{uuid}")
    public ResponseEntity<DTO> updateEntity(@RequestBody DTO dto, @PathVariable UUID uuid) {
        return ResponseEntity.ok(service.updateEntity(dto, uuid));
    }

    // DELETE - DELETE > http://localhost:8081/?/{id}
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID uuid) {
        try {
            service.deleteEntity(uuid);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
