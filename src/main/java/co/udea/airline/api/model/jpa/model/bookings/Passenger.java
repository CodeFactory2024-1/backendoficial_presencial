package co.udea.airline.api.model.jpa.model.bookings;

import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passenger")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_booking")
    private Booking booking;

    @OneToOne(mappedBy = "passenger",
            cascade = {CascadeType.MERGE},  fetch = FetchType.EAGER)
    private SeatXPassenger seatXPassenger;


}
