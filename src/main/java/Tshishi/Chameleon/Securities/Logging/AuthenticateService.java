package Tshishi.Chameleon.Securities.Logging;

import Tshishi.Chameleon.Securities.Config.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthenticateService(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogger(), request.getPassword()));
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getLogger());

        final String token = jwtService.generateToken(userDetails);
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
