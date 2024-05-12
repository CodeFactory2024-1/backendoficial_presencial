package co.udea.airline.api.model.jpa.repository.vehicles;

import co.udea.airline.api.model.jpa.model.vehicles.Aircraft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Integer> {

    @Query("SELECT a FROM Aircraft a WHERE UPPER(a.name) = UPPER(:name)")
    Optional<Aircraft> findByName(@Param("name") String name);
}
