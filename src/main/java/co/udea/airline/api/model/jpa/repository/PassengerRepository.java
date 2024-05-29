package co.udea.airline.api.model.jpa.repository;

import co.udea.airline.api.model.jpa.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
