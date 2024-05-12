package co.udea.airline.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

//@EnableTransactionManagement
@SpringBootApplication
@ComponentScan({
        "co.udea.airline.api.controller",
        "co.udea.airline.api.model.jpa",
        "co.udea.airline.api.filter",
        "co.udea.airline.api.service",
        "co.udea.airline.api.utils",
        "co.udea.airline.api.dto"
})
public class AirlineApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AirlineApiApplication.class, args);
    }

}
