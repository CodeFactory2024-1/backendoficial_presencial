package co.udea.airline.api.model.jpa.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.model.Privilege;

@Projection(name = "withPrivileges", types = { Position.class })
public interface WithPrivilegesAndId {

    Long getPositionId();
    
    String getName();

    String getDescription();

    List<Privilege> getPrivileges();

}
