package Tshishi.Chameleon.Securities.Logging;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
