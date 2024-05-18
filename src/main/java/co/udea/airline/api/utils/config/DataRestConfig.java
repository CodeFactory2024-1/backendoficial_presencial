package co.udea.airline.api.utils.config;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ConfigurableHttpMethods;
import org.springframework.data.rest.core.mapping.ExposureConfigurer.AggregateResourceHttpMethodsFilter;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import co.udea.airline.api.model.jpa.model.IdentificationType;
import co.udea.airline.api.model.jpa.model.Position;
import co.udea.airline.api.model.jpa.model.Privilege;

@Component
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.useHalAsDefaultJsonMediaType(false);
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);

        config.exposeIdsFor(Position.class, Privilege.class);

        config.getExposureConfiguration()
                .withCollectionExposure(idMethodsFilter(false))
                .withItemExposure(idMethodsFilter(true));
    }

    AggregateResourceHttpMethodsFilter idMethodsFilter(boolean all) {
        return (metdata, httpMethods) -> {
            if (metdata.getDomainType().equals(IdentificationType.class)) {
                if (all)
                    return ConfigurableHttpMethods.NONE;
                else
                    return ConfigurableHttpMethods.NONE.enable(HttpMethod.GET);
            }
            return httpMethods;
        };
    }

}
