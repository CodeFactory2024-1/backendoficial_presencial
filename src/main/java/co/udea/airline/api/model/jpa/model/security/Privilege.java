package co.udea.airline.api.model.jpa.model.security;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
@Table(name="PRIVILEGE")
public class Privilege implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRIVILEGE_ID")
    private Long privilegeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String detail;

    @Override
    public String getAuthority() {
        return getName().toLowerCase();
    }
}
