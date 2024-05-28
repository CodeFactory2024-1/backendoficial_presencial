package co.udea.airline.api.model.jpa.model.flightbmodel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a flight in the airline system.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(name = "flight_number", nullable = false, unique = true, updatable = true, length = 8)
    private String flightNumber;

    @NotNull
    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @NotNull
    @Column(name = "tax_percent", nullable = false)
    private double taxPercentage;

    @NotNull
    @Column(name = "surcharge", nullable = false)
    private double surcharge;

    @OneToMany( fetch = FetchType.EAGER,mappedBy = "flight" )
    private Set<Scale> scales;


    @Column(name = "flight_type")
    private String flightType;   
}
