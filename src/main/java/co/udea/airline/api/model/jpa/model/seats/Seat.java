package co.udea.airline.api.model.jpa.model.seats;

import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.utils.common.SeatClassEnum;
import co.udea.airline.api.utils.common.SeatLocationEnum;
import co.udea.airline.api.utils.common.SeatStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="seat")
public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seat")
    private Long id;

    @NotNull
    @Column(name = "tag")
    private String tag;

    @NotNull
    @Column(name = "status")
    private SeatStatusEnum status = SeatStatusEnum.AVAILABLE;

    @NotNull
    @Column(name = "class")
    private SeatClassEnum seatClass;

    @NotNull
    @Column(name = "location")
    private SeatLocationEnum location;

    @NotNull
    @Column(name = "seat_number")
    private int seatNumber;

    @NotNull
    @Column(name = "surcharge")
    private BigDecimal surcharge = new BigDecimal("0.0");

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="flight_id")
    private Flight flight;

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private SeatXPassenger seatXPassenger;

    public BigDecimal calculateTotalSurcharge(){
        if (this.seatClass == null || this.location == null) {
            return null;
        }
        BigDecimal seatClassSurcharge = this.seatClass.getSurcharge();
        BigDecimal seatLocationSurcharge = this.location.getSurcharge();
        return seatClassSurcharge.add(seatLocationSurcharge);
    }

}


