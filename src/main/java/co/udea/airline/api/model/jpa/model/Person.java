package co.udea.airline.api.model.jpa.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PERSON")
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    private Integer personId;

    @ManyToOne
    @JoinColumn(name = "ID_IDENTIFICATION_TYPE")
    private IdentificationType identificationType;

    @Column(name = "verification_code", length = 64)
    @JsonIgnore
    private String verificationCode;

    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    private Character genre;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "CITY")
    private String city;
    @Column(name = "ADDRESS")
    private String address;

    @NotBlank
    @Email
    private String email;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name = "EXTERNAL_LOGIN_SOURCE")
    @JsonIgnore
    private String externalLoginSource;

    @JsonIgnore
    private Boolean enabled;
    
    @JsonIgnore
    private Boolean verified;

    @Column(name = "FAILED_LOGIN_ATTEMPTS")
    @JsonIgnore
    private Integer failedLoginAttempts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PERSON_POSITION", joinColumns = @JoinColumn(name = "PERSON_ID"), inverseJoinColumns = @JoinColumn(name = "POSITION_ID"))
    private List<Position> positions;

    @JsonIgnore
    public Set<Privilege> getPrivileges() {
        if (getPositions() == null) {
            return Set.of();
        }
        return getPositions().stream()
                .map(pos -> pos.getPrivileges())
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // prefix all roles with 'ROLE_' and uppercase them
        Set<GrantedAuthority> authorities = getPositions().stream()
                .map(pos -> new SimpleGrantedAuthority("ROLE_".concat(pos.getName().toUpperCase())))
                .collect(Collectors.toSet());

        // add all privleges
        authorities.addAll(getPrivileges());

        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getEmail();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }
    @JsonIgnore
    public boolean isVerified() {
        return verified;
    }

}
