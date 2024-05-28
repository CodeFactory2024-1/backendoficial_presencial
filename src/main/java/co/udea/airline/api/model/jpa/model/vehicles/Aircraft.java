package co.udea.airline.api.model.jpa.model.vehicles;

import co.udea.airline.api.utils.common.VehicleStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "ability")
    private String ability;

    @NotNull
    @Column(name = "state")
    private VehicleStatusEnum state = VehicleStatusEnum.active;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "allows_pets")
    private Boolean allowsPets = Boolean.TRUE;

    @Column(name = "admission_date")
    private LocalDateTime admissionDate;

    @Column(name = "seats")
    private Integer seats;

  //Constructor
    //Setter
    //Getter
    //Si es posible usar Loombok


    public Aircraft() {
    }

    public Aircraft(Integer id, String name, String ability, VehicleStatusEnum state, String model, Boolean allowsPets, LocalDateTime admissionDate, Integer seats) {
        this.id = id;
        this.name = name;
        this.ability = ability;
        this.state = state;
        this.model = model;
        this.allowsPets = allowsPets;
        this.admissionDate = admissionDate;
        this.seats = seats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public VehicleStatusEnum getState() {
        return state;
    }

    public void setState(VehicleStatusEnum state) {
        this.state = state;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getAllowsPets() {
        return allowsPets;
    }

    public void setAllowsPets(Boolean allowsPets) {
        this.allowsPets = allowsPets;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
