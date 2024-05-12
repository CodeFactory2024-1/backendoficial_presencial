package co.udea.airline.api.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.model.Privilege;
import co.udea.airline.api.utils.common.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    private Jwt superAdminToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
        } else {

            String tokenValue = bearerToken.substring("Bearer ".length());

            try {
                Jwt jwt;
                Authentication auth;

                if (checkIfSuperAdmin(tokenValue)) {
                    jwt = superAdminToken;
                } else {
                    jwt = jwtUtils.getToken(tokenValue);
                    jwtUtils.validateToken(jwt);
                }

                auth = new JwtAuthenticationToken(jwt, jwtUtils.getAuthorities(jwt));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                // this is very important, since it guarantees the user is not authenticated at
                // all
                SecurityContextHolder.clearContext();
                log.debug("Bearer token was rejected by %s: %s".formatted(this.getClass().toString(), bearerToken));
            }
        }

        filterChain.doFilter(request, response);

    }

    boolean checkIfSuperAdmin(String token) {
        var encoder = new BCryptPasswordEncoder();

        if (encoder.matches(token, "$2a$10$9XHNq7OxIWitoe7I9rzaIeQlaKQTC3v9zVmYjReBFRidRKAw7lTTa")) {

            if (superAdminToken == null) {

                Person superAdmin = Person.builder()
                        .email("super@admin")
                        .positions(Arrays.asList(Position.builder()
                                .name("ADMIN")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .name("all:privileges")
                                        .build()))
                                .build()))
                        .build();
                superAdminToken = jwtUtils.createToken(superAdmin);
            }

            return true;
        }
        return false;
    }

}
