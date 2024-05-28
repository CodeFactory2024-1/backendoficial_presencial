package co.udea.airline.api.services.bookingservices;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookingService {
  @Value("${airline-api.bookings.api-url}")
  private String airlineApiBookingsApiUrl;
  
  public boolean flightHasBookings(Long flightId) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(airlineApiBookingsApiUrl + flightId))
        .build();
    try {
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException("Error checking bookings for flight " + flightId);
      }

      String responseBody = response.body();
      ObjectMapper mapper = new ObjectMapper();

      @SuppressWarnings("unchecked")
      List<Object> bookings = mapper.readValue(responseBody, List.class);

      return !bookings.isEmpty();
    } catch (Exception e) {
      throw new RuntimeException("Error checking bookings for flight " + flightId, e);
    }
  }
}