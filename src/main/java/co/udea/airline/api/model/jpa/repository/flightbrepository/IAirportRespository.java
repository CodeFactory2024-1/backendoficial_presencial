package co.udea.airline.api.model.jpa.repository.flightbrepository;

import co.udea.airline.api.model.jpa.model.flightbmodel.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface IAirportRespository extends JpaRepository<Airport, String> {
    @Query("SELECT a FROM Airport a WHERE a.id = ?1")
    public Airport findByIATA(String IATACode);

}
