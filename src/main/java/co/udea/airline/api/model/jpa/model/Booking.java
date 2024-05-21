package co.udea.airline.api.model.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long bookingId;

    @Column(name = "FLIGHT_ID")
    private Long flightId;

    @Column(name = "BOOKING_DATE")
    private Date booking_date;

    @Column(name = "BOOKING_STATUS")
    private String booking_status;

    @Column(name = "TOTAL_PRICE")
    private Long total_price;
}
