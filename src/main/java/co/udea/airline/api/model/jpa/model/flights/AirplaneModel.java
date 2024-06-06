package co.udea.airline.api.model.jpa.model.flights;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an airplane model.
 * 
 * This class is used to store information about an airplane model, including
 * its ID, family, capacity, cargo capacity, volume capacity, and associated
 * scales.
 * 
 * @param id             The ID of the airplane model.
 * @param family         The family of the airplane model.
 * @param capacity       The capacity of the airplane model.
 * @param cargoCapacity  The cargo capacity of the airplane model.
 * @param volumeCapacity The volume capacity of the airplane model.
 * @param scales         The set of scales associated with the airplane model.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "AIRPLANE_MODEL")
public class AirplaneModel implements java.io.Serializable {

    @Id
    @Column(name = "airplane_model")
    private String id;

    @Column(name = "family")
    private String family;

    @Column(name = "capacity", precision = 3)
    private int capacity;

    @Column(name = "cargo_capacity", precision = 10, scale = 2)
    private long cargoCapacity;

    @Column(name = "volume_capacity", precision = 10, scale = 2)
    private long volumeCapacity;

    @OneToMany(mappedBy = "airplaneModel")
    private Set<Scale> scales = new HashSet<Scale>();
}
