package co.udea.airline.api.model.jpa.model.flights;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.udea.airline.api.model.jpa.model.seats.Seat;
import co.udea.airline.api.utils.common.FlightTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


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

    @NotNull
    @Column(name = "flight_type")
    private FlightTypeEnum flightType = FlightTypeEnum.Domestic;

    @JsonIgnore
    @OneToMany(mappedBy = "flight", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Seat> seatList = new ArrayList<>();
}