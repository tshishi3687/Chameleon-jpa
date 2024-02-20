package Tshishi.Chameleon.Securities.Config;

import Tshishi.Chameleon.Securities.ConstParam;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurer {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
                                        .requestMatchers("/", "/favicon.ico", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
//                                        .requestMatchers(HttpMethod.POST, "/mobembo/v1/auth/**", "/personne/email", "/personne/change_passe", "/personne/mdp_modif", "/personne/activation_compte").permitAll()
//                                        .requestMatchers(HttpMethod.GET, "/bien/count","/mobembo/v1/auth/**", "/bien/allBiens", "/province", "/ville", "/typebien", "/personne/nbcompte", "/pays").permitAll()
//                                        .requestMatchers(HttpMethod.POST, "/ville/*", "/province/*", "/service/*", "/typebien/*", "/type/*", "/personne").hasAuthority("Admin")
//                                        .requestMatchers(HttpMethod.DELETE, "/ville/", "/province/", "/service/", "/typebien/", "/type/", "/personne").hasAuthority("Admin")
//                                        .requestMatchers(HttpMethod.GET, "/service/*", "/type_bien/*", "/type/*", "/personne").hasAuthority("Admin")
//                                        .requestMatchers(HttpMethod.POST, "/service/*", "/type_bien/*", "/type/*", "/personne").hasAuthority("Admin")
//                                        .requestMatchers(HttpMethod.DELETE, "/service/", "/type_bien/", "/type/", "/personne").hasAuthority("Admin")
//                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                                        .anyRequest().authenticated()
                                        .and()
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .and()
                                        .authenticationProvider(authenticationProvider)
                                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("http://localhost:4200");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader(ConstParam.JWT_NAME);
        corsConfiguration.applyPermitDefaultValues();
        return request -> corsConfiguration;
    }
}
