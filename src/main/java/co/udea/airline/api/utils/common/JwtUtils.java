package co.udea.airline.api.utils.common;

import java.security.KeyPair;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import co.udea.airline.api.model.jpa.model.security.Person;
import co.udea.airline.api.model.jpa.model.security.Position;
import co.udea.airline.api.model.jpa.model.security.Privilege;

@Component
public class JwtUtils {

    private static final String ROLES_IDENTIFIER = "roles";
    private static final String PRIVILEGES_IDENTIFIER = "privileges";
    private static final String EMAIL_IDENTIFIER = "email";

    final JwtEncoder jwtEncoder;

    final JwtDecoder jwtDecoder;

    final KeyPair keyPair;

    public JwtUtils(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, KeyPair keyPair) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.keyPair = keyPair;
    }

    private final Long EXPIRATION = 8 * 60 * 60L; // in seconds

    /**
     * Creates a JWT that contains the currently autthenticated user's email, roles,
     * privileges, and other info like the time when the token expires.
     * 
     * @param person The {@link Person} from where the token is created.
     * @return A {@link Jwt}
     */
    public Jwt createToken(Person person) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(person.getEmail())
                .claim(ROLES_IDENTIFIER, person.getPositions().stream()
                        .map(Position::getName).collect(Collectors.toList()))
                .claim(PRIVILEGES_IDENTIFIER, person.getPrivileges().stream()
                        .map(Privilege::getName).collect(Collectors.toList()))
                .issuer("https://airline-api.com")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRATION))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));

    }

    /**
     * Decodes a token from it's specified string form
     * 
     * @param token The token in a string form
     * @return The {@link Jwt} decoded by the internal {@link JwtDecoder}
     */
    public Jwt getToken(String token) {
        return jwtDecoder.decode(token);
    }

    /**
     * Builds a list of roles and privileges from the {@link Jwt} specified, where
     * each role is uppercased and prefixed with 'ROLE_', and each privilege is
     * lowercased
     * 
     * @param jwt The token from where the list is built
     * @return A {@link List} of {@link GrantedAuthority} cointaining both roles and
     *         privileges of a person/user
     */
    public List<GrantedAuthority> getAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(ROLES_IDENTIFIER);
        List<String> privileges = jwt.getClaimAsStringList(PRIVILEGES_IDENTIFIER);

        List<GrantedAuthority> authorities = roles.stream()
                .map(roleStr -> new SimpleGrantedAuthority("ROLE_".concat(roleStr)))
                .collect(Collectors.toList());

        authorities.addAll(privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .toList());

        return authorities;
    }

    public List<String> getRoles(Jwt jwt) {
        return jwt.getClaimAsStringList(ROLES_IDENTIFIER)
                .stream()
                .map(role -> role.replace("ROLE_", ""))
                .toList();
    }



    /**
     * Checks through the token's signature if the token is indeed generated by our
     * server, and also if the token itself hasn't expired
     * 
     * @param jwt
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(Jwt jwt) {
        return Instant.now().isAfter(jwt.getExpiresAt());
    }

}

