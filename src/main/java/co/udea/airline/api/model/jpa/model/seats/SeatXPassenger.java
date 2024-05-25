package co.udea.airline.api.model.jpa.model.seats;

import co.udea.airline.api.model.jpa.model.bookings.Passenger;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat_x_passenger")
public class SeatXPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seatxpassenger")
    private Long id;

    @NotNull
    @OneToOne(cascade = {CascadeType.REFRESH},  fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger")
    private Passenger passenger;

    @NotNull
    @OneToOne(cascade = {CascadeType.REFRESH},  fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seat")
    private Seat seat;

}
