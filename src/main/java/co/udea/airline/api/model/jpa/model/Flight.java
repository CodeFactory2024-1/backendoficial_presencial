package co.udea.airline.api.model.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLIGHT_ID")
    private Long flightId;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "BASE_PRICE")
    private Long basePrice;

    @Column(name = "TAX_PERCENT")
    private Long taxPercentage;

    private Long surcharge;

    private String status;
}
