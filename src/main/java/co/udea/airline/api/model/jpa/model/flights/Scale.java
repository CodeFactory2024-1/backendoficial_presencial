package co.udea.airline.api.model.jpa.model.flights;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an Scale data transfer object (DTO).
 * This class contains information about an Scale.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "SCALE")
public class Scale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scale_id")
    private long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "origin_airport", nullable = false)
    private Airport originAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport", nullable = false)
    private Airport destinationAirport;

    @ManyToOne
    @JoinColumn(name = "airplane_model", nullable = false)
    private AirplaneModel airplaneModel;

    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDate;

    @Column(name = "arrival_date", nullable = false)
    private LocalDateTime arrivalDate;
}
