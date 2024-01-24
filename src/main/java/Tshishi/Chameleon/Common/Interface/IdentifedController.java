package Tshishi.Chameleon.Common.Interface;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;

public interface IdentifedController<DTO extends IdentifiedDto<UUID>, UUID> {

    // Create
    ResponseEntity<DTO> addEntity(@RequestBody @Validated DTO dto, HttpServletRequest request) throws URISyntaxException;

    // Read
    ResponseEntity<DTO> readEntity(@PathVariable UUID UUID);

    ResponseEntity<List<DTO>> readAllEntities();

    // Update
    ResponseEntity<DTO> updateEntity(@RequestBody DTO dto, @PathVariable UUID UUID);

    // Delete
    ResponseEntity<Void> deleteEntity(@PathVariable UUID UUID);
}
