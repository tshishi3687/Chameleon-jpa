package Tshishi.Chameleon.Securities.Logging;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/v01/auth")
@Tag(name = "Auth Controller", description = "API for managing auth")
public class AuthenticationController {

    private final AuthenticateService service;

    public AuthenticationController(AuthenticateService service) {
        this.service = service;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(service.authenticate(request, response));
    }
}
