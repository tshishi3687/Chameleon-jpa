package Tshishi.Chameleon.Securities.Logging;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v01/auth")
@Tag(name = "Auth Controller", description = "API for managing auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateService service;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) {
        return ResponseEntity.ok(service.authenticate(request, response));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken() {
        return ResponseEntity.ok(service.validateToken());
    }
}
