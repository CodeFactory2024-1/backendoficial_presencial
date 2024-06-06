package co.udea.airline.api.model.jpa.model.flights;

import java.util.Set;

import co.udea.airline.api.utils.common.FlightTypeEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an flight data transfer object (DTO).
 * This class contains information about an flight.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "FLIGHT")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private long id;

    @Column(name = "flight_number", unique = true)
    private String flightNumber;

    @Column(name = "base_price", nullable = false, precision = 22, scale = 0)
    private double basePrice;

    @Column(name = "tax_percent")
    private double taxPercent;

    @Column(name = "surcharge")
    private double surcharge;

    @Column(name = "flight_type")
    private FlightTypeEnum flightType = FlightTypeEnum.Domestic;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Scale> scales;

    public void generateFlightNumber() {
        final String AIRLINE_CODE = "SA";
        String randomNumber = String.format("%04d", (int) (Math.random() * 10000));

        this.flightNumber = AIRLINE_CODE + randomNumber;
    }

    public void generateFlightType() {
        this.flightType = FlightTypeEnum.Domestic;
        String originAirportCountry = this.scales.iterator().next().getOriginAirport().getCountry();

        if (originAirportCountry != null) {
            for (Scale scale : this.scales) {
                String destinationAiportCountry = scale.getDestinationAirport().getCountry();

                if (!destinationAiportCountry.equals(originAirportCountry)) {
                    this.flightType = FlightTypeEnum.International;
                    break;
                }
            }
        }
    }
}
