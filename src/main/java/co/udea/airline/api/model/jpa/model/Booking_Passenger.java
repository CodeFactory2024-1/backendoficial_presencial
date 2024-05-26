package co.udea.airline.api.model.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Booking_Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_PASSENGER_ID")
    private Long booking_passenger_id;

    @ManyToOne
    @JoinColumn(name = "BOOKING_ID")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "PASSENGER_ID")
    private Passenger passenger;
}