package co.udea.airline.api.model.jpa.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "POSITION", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class Position implements Serializable { // == Role

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSITION_ID")
    @JsonProperty(access = Access.READ_ONLY, index = 1)
    private Long positionId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "POSITION_PRIVILEGE", joinColumns = @JoinColumn(name = "POSITION_ID"), inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID"))
    @RestResource(exported = false)
    @AccessType(Type.PROPERTY)
    @jakarta.persistence.Access(jakarta.persistence.AccessType.PROPERTY)
    private List<Privilege> privileges;

}
