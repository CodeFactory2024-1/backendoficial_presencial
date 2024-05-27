package co.udea.airline.api.utils.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class for intercepting request and validating the 'Authorization' header to
 * set the SecurityContext appropriately
 */
@Component
@Slf4j
public class JWTTokenFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    /**
     * Configures the {@link #jwtDecoder} based on a provided RSA public key
     */
    public JWTTokenFilter(@Value("${airline-api.jwt.rsa-pub-key:}") Resource publicKeyFile)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        byte[] keyBytesPub = FileCopyUtils.copyToByteArray(publicKeyFile.getInputStream());
        X509EncodedKeySpec specPub = new X509EncodedKeySpec(keyBytesPub);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(specPub);

        jwtDecoder = NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }

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
                if ((Instant.now().isAfter(jwt.getExpiresAt())))
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