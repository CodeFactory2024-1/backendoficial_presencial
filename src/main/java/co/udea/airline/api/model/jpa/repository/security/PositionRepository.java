package co.udea.airline.api.model.jpa.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.udea.airline.api.model.jpa.model.security.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByName(String name);

}
