package co.udea.airline.api.services.flightsservices;


import co.udea.airline.api.services.bookingservices.BookingService;
import co.udea.airline.api.model.jpa.model.flightbmodel.Flight;
import co.udea.airline.api.model.jpa.model.flightbmodel.Scale;

import co.udea.airline.api.model.jpa.repository.flightbrepository.IFlightDetailsProjection;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IFlightProjection;
import co.udea.airline.api.model.jpa.repository.flightbrepository.IFlightsRepository;


import co.udea.airline.api.model.jpa.repository.flightbrepository.IScaleRespository;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Iterator;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class FlightServices {


    @Autowired
    private IFlightsRepository flightRepository;

    @Autowired
    private ScaleServices scaleServices;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IFlightDetailsProjection flightDetailsProjection;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private IScaleRespository scaleRepository;


    public List<Flight> searchFlights() {
        return flightRepository.findAll();
    }
    public List<IFlightProjection> searchFlightGeneral() {
        return flightRepository.findAllFlightGeneral();
    }

    public List<IFlightDetailsProjection> searchFlightDetailsByFlightNumber(String flightNumber) {
        return flightRepository.findFlightDetailsByFlightNumber(flightNumber);
    }

    public Flight addFlight(Flight flight) {
        Flight savedFlight = null;
        try {
            savedFlight = flightRepository.save(flight);;
            Set<Scale> scales = flight.getScales();
            if (scales != null && !scales.isEmpty()) {
                for (Scale scale : scales) {
                    scale.setFlight(savedFlight);
                    scaleServices.saveScale(scale);
                }
                savedFlight.setScales(scales);
            }
        } catch (Exception e) {
            System.out.println("Error al guardar el vuelo: " + e);
        }
        return savedFlight;
    }


    public void deleteFlight(Long id) {
        try {
            scaleRepository.deleteScaleByFlightId(id);
            flightRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró el vuelo con id: " + id);
        } catch (Exception e) {
            System.out.println("Error al eliminar el vuelo con id: " + id + " debido a  que cuenta con almenos una reserva asociada");
        }
    }

    public List<Flight> searchFlight(Long id) {
        Flight flight = flightRepository.findById(id).orElse(null);
        return flight != null ? Collections.singletonList(flight) : Collections.<Flight>emptyList();
    }

    public Flight updateFlight(String flightNumber, Flight flight) {
        Flight updatedFlight = getFlightByFlightNumber(flightNumber);

        if (updatedFlight == null) {
            return null;
        }

        boolean hasBookings = bookingService.flightHasBookings(updatedFlight.getId());

        if (hasBookings) {
            Iterator<Scale> scalesIterator = flight.getScales().iterator();

            if (flight.getScales().size() != updatedFlight.getScales().size()) {
                throw new IllegalArgumentException("The flight has bookings, you can't change the number of scales.");
            }

            for (Scale scale : updatedFlight.getScales()) {
                scale.setAirplaneModel(scalesIterator.next().getAirplaneModel());
            }

            if (!flight.equals(updatedFlight)) {
                throw new IllegalArgumentException("The flight has bookings, you can only change the airplane model.");
            }
        }

        updatedFlight.setBasePrice(flight.getBasePrice());
        updatedFlight.setTaxPercentage(flight.getTaxPercentage());
        updatedFlight.setSurcharge(flight.getSurcharge());

        updatedFlight = flightRepository.save(updatedFlight);

        Set<Scale> scales = flight.getScales();

        Set<Long> oldScalesIds = updatedFlight.getScales().stream().map(Scale::getId).collect(Collectors.toSet());
        Set<Long> newScalesIds = scales.stream().map(Scale::getId).collect(Collectors.toSet());

        for (Long scaleId : oldScalesIds) {
            if (!newScalesIds.contains(scaleId)) {
                scaleServices.deleteScaleById(scaleId);
            }

            // TODO: Validate if the new scales number is greater than zero
        }

        for (Scale scale : scales) {
            scale.setFlight(updatedFlight);
            scale = scaleServices.updateScale(scale);
        }

        updatedFlight.getFlightType();
        updatedFlight = flightRepository.save(updatedFlight);

        return updatedFlight;
    }

    public Flight getFlightByFlightNumber(String flightNumber) {
        List<Flight> flights = flightRepository.findByFlightNumber(flightNumber);
        if (flights.size() == 0) {
            return null;
        } else {
            return flights.get(0);
        }
    }
    public Flight deleteFlightById(Long id) {
        Flight deletedFlight = getFlightById(id);
        if (deletedFlight == null) {
            return null;
        }
        flightRepository.deleteById(id);
        return deletedFlight;
    }
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElse(null);
    }
    public Flight deleteFlightByFlightNumber(String flightNumber) {
        Flight deletedFlight = getFlightByFlightNumber(flightNumber);
        if (deletedFlight == null) {
            return null;
        }
        deleteFlightById(deletedFlight.getId());
        return deletedFlight;
    }
}
