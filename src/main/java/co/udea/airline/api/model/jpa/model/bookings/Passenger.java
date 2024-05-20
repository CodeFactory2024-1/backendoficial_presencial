package co.udea.airline.api.model.jpa.model.bookings;

import co.udea.airline.api.model.jpa.model.seats.SeatXPassenger;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passenger")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "id_booking")
    private Booking booking;

    @OneToOne(mappedBy = "passenger", cascade = CascadeType.ALL)
    private SeatXPassenger seatXPassenger;


}
