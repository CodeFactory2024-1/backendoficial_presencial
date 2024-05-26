package com.udea.vuelo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Definición de la excepción personalizada para errores de lectura de JSON
class JsonReadException extends RuntimeException {
    public JsonReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
@Service
public class FlightService {
    //Ruta del archivo
    private static final String FILE_PATH = "flights.json";
    private static final String JSON_READ_ERROR_MSG = "Error leyendo el archivo JSON";

    private final ObjectMapper objectMapper;

    public FlightService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //Método de la lógica de búsqueda de vuelos
    public List<List<Flight>> searchFlightsByDate(LocalDate startDate, LocalDate endDate) {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);
            if(inputStream != null) {
                Flight[] flights = objectMapper.readValue(inputStream, Flight[].class);
                return Arrays.asList(
                    Arrays.stream(flights)
                        .filter(flight -> isDateInRange(flight.getDepartureDate(), startDate, endDate))
                        .toList()); // Utilizar toList() en lugar de Collectors.toList()
            }
        } catch (IOException e) {
            throw new JsonReadException(JSON_READ_ERROR_MSG, e);
        }
        return new ArrayList<>(); // Devolver una lista vacía si hay una excepción o si inputStream es nulo
    }



    private boolean isDateInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        // Verifica si la fecha está en el rango correcto
        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }

   public List<List<Flight>> searchFlightsByTotalCost(int startCost, int endCost) {
       try {
           InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);

           if (inputStream != null) {
               Flight[] flights = objectMapper.readValue(inputStream, Flight[].class);
               return Arrays.stream(flights)
                       .filter(flight -> isCostInRange(flight.getPrice().getTotalCost(), startCost, endCost))
                       .collect(Collectors.groupingBy(Flight::getAirline))
                       .values()
                       .stream()
                       .map(flightList -> flightList.stream().sorted(Comparator.comparingInt(flight -> flight.getPrice().getTotalCost())).toList())
                       .toList();
           } else {
               return Collections.emptyList();
           }
       } catch (IOException e) {
           throw new JsonReadException(JSON_READ_ERROR_MSG, e);
       }
   }

    private boolean isCostInRange(int cost, int start, int end) {
        return cost >= start && cost <= end;
    }

    public List<List<Flight>> searchFlightsByRoute(String origin, String destination) {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);

            if(inputStream != null) {
                Flight[] flights = objectMapper.readValue(inputStream, Flight[].class);
                return Arrays.asList(
                        Arrays.stream(flights)
                                .filter(flight -> isRouteInRoutes(flight.getOrigin(),flight.getDestination(),origin, destination))
                                .toList());
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            throw new JsonReadException(JSON_READ_ERROR_MSG, e);

        }
    }

    private boolean isRouteInRoutes(String origintocheck,String destinationtocheck, String origin, String destination) {
        // Verifica si el precio está en el rango correcto
        return Objects.equals(origintocheck, origin) && Objects.equals(destinationtocheck, destination);
    }

    public List<List<Flight>> searchFlightsByAirline(String airline) {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);

            if(inputStream != null) {
                Flight[] flights = objectMapper.readValue(inputStream, Flight[].class);
                return Arrays.asList(
                        Arrays.stream(flights)
                                .filter(flight -> isAirlineInFlights(flight.getAirline(),airline))
                                .toList());
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            throw new JsonReadException(JSON_READ_ERROR_MSG, e);

        }
    }

    private boolean isAirlineInFlights(String airlinetocheck, String airline ) {
        // Verifica si el precio está en el rango correcto
        return Objects.equals(airlinetocheck, airline);
    }

    public Price searchPriceById(int id) {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);

            if (inputStream != null) {
                Flight[] flights = objectMapper.readValue(inputStream, Flight[].class);
                for (Flight flight : flights) {
                    if (flight.getId() == id) {
                        return flight.getPrice(); // Devuelve directamente el objeto Price
                    }
                }
            }
            return null; // Indica que no se encontró el vuelo con el ID dado
        } catch (IOException e) {
            throw new JsonReadException(JSON_READ_ERROR_MSG, e);

        }
    }






}
