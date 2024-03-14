package Tshishi.Chameleon.Securities.Logging;

import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.UsersRepository;
import Tshishi.Chameleon.Securities.Config.JwtAuthenticationFilter;
import Tshishi.Chameleon.Securities.Config.JwtService;
import Tshishi.Chameleon.Securities.ConstParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsersRepository usersRepository;
    private final JwtAuthenticationFilter jwtAuth;
    private final HttpServletRequest request;

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

    public ResponseEntity<?> validateToken() {
        final String authHeader = request.getHeader(ConstParam.JWT_NAME);
        String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid JWT token");
        }
        jwt = authHeader.replace(ConstParam.BEARER, "");
        return ResponseEntity.ok(jwtService.validateToken(jwt, usersRepository.findUsersByMailOrPhone(jwtAuth.userEmail, jwtAuth.userEmail).orElseThrow()));
    }
}
