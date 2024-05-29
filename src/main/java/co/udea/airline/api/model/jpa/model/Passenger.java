package co.udea.airline.api.model.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PASSENGER_ID")
    private Long passengerId;
    
    @Column(name = "PERSON_ID")
    private Long personId;

}
