package co.udea.airline.api.services.flights.service;

import co.udea.airline.api.model.jpa.model.flights.Flight;
import co.udea.airline.api.model.jpa.model.flights.Scale;
import co.udea.airline.api.model.jpa.repository.flights.IFlightRepository;
import co.udea.airline.api.services.bookings.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private IFlightRepository flightRepository;

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private BookingService bookingService;

    public Flight saveFlight(Flight flight) {

        flight.generateFlightNumber();

        Flight flightSaved = flightRepository.save(flight);

        Set<Scale> scales = flight.getScales();
        for (Scale scale : scales) {
            scale.setFlight(flightSaved);
            scale = scaleService.saveScale(scale);
        }

        flight.setScales(scales);
        flight.setId(flightSaved.getId());
        flight.generateFlightType();
        flightSaved = flightRepository.save(flight);

        return flightSaved;
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElse(null);
    }

    public Flight getFlightByFlightNumber(String flightNumber) {
        List<Flight> flights = flightRepository.findByFlightNumber(flightNumber);
        if (flights.size() == 0) {
            return null;
        } else {
            return flights.get(0);
        }
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
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
        updatedFlight.setTaxPercent(flight.getTaxPercent());
        updatedFlight.setSurcharge(flight.getSurcharge());

        updatedFlight = flightRepository.save(updatedFlight);

        Set<Scale> scales = flight.getScales();

        Set<Long> oldScalesIds = updatedFlight.getScales().stream().map(Scale::getId).collect(Collectors.toSet());
        Set<Long> newScalesIds = scales.stream().map(Scale::getId).collect(Collectors.toSet());

        for (Long scaleId : oldScalesIds) {
            if (!newScalesIds.contains(scaleId)) {
                scaleService.deleteScaleById(scaleId);
            }

            // TODO: Validate if the new scales number is greater than zero
        }

        for (Scale scale : scales) {
            scale.setFlight(updatedFlight);
            scale = scaleService.updateScale(scale);
        }

        updatedFlight.generateFlightType();
        updatedFlight = flightRepository.save(updatedFlight);

        return updatedFlight;
    }

    public Flight deleteFlightById(Long id) {
        Flight deletedFlight = getFlightById(id);

        if (deletedFlight == null) {
            return null;
        }

        boolean hasBookings = bookingService.flightHasBookings(id);
        if (hasBookings) {
            throw new IllegalArgumentException("The flight has bookings, you can't delete it.");
        }

        flightRepository.deleteById(id);
        return deletedFlight;
    }

    public Flight deleteFlightByFlightNumber(String flightNumber) {
        Flight deletedFlight = getFlightByFlightNumber(flightNumber);

        if (deletedFlight == null) {
            return null;
        }

        boolean hasBookings = bookingService.flightHasBookings(deletedFlight.getId());
        if (hasBookings) {
            throw new IllegalArgumentException("The flight has bookings, you can't delete it.");
        }

        flightRepository.deleteById(deletedFlight.getId());

        return deletedFlight;
    }
}
