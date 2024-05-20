package co.udea.airline.api.utils.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Class for intercepting request and validating the 'Authorization' header to
 * set the SecurityContext appropiately
 */
@Component
@Slf4j
public class JWTTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtDecoder jwtDecoder;
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

                    jwt = jwtDecoder.decode(tokenValue);
                    if((Instant.now().isAfter(jwt.getExpiresAt())))
                        throw new JwtException("Invalid JWT");


                auth = new JwtAuthenticationToken(jwt, getAuthorities(jwt));
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

    public List<GrantedAuthority> getAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        List<String> privileges = jwt.getClaimAsStringList("privileges");

        List<GrantedAuthority> authorities = roles.stream()
                .map(roleStr -> new SimpleGrantedAuthority("ROLE_".concat(roleStr)))
                .collect(Collectors.toList());

        authorities.addAll(privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .toList());

        return authorities;
    }

    public List<String> getRoles(Jwt jwt) {
        return jwt.getClaimAsStringList("roles")
                .stream()
                .map(role -> role.replace("ROLE_", ""))
                .toList();
    }

}