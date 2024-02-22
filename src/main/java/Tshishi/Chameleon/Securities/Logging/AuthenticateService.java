package Tshishi.Chameleon.Securities.Logging;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.UsersRepository;
import Tshishi.Chameleon.Securities.Config.AuthenticationResponse;
import Tshishi.Chameleon.Securities.Config.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final JwtService jwtService;

    public AuthenticateService(AuthenticationManager authenticationManager, UsersRepository usersRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.jwtService = jwtService;
    }

    public void authenticate(AuthenticationRequest request, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogger(),
                            request.getPassword()
                    )
            );

            Users users = usersRepository.findUsersByMailOrPhoneOrBusinessNumber(request.getLogger(), request.getLogger(), request.getLogger()).orElseThrow();
            String jwtToken = jwtService.generateToken(users);

            response.addHeader("Authorization", "Bearer " + jwtToken);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Authentication successful");

        } catch (Exception e) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Email or password is incorrect");
        }
    }
}
