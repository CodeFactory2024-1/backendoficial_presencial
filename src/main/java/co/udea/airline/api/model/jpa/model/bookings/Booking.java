package co.udea.airline.api.model.jpa.model.bookings;


import java.util.ArrayList;
import java.util.List;

import co.udea.airline.api.model.jpa.model.flights.Flight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    private Long id;

    @NotNull
    @Column(name = "codename")
    private String codename;

    @JsonIgnore
    @OneToMany(mappedBy = "booking",
            cascade = {CascadeType.MERGE},  fetch = FetchType.EAGER)
    private List<Passenger> passengerList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE},  fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;

}