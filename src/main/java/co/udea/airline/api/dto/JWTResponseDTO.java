package co.udea.airline.api.dto;

import java.time.ZonedDateTime;

public record JWTResponseDTO(String email, ZonedDateTime expiresAt, String token) {

}
