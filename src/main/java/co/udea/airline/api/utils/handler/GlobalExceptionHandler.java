package co.udea.airline.api.utils.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.udea.airline.api.utils.common.StandardResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        "Insufficient permissions to access resource."),
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    protected ResponseEntity<?> handleAuthenticationCredentialsNotFoundException(
            AuthenticationCredentialsNotFoundException ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        "User not authenticated"),
                HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<?> handleBadCredentialException(BadCredentialsException ex) {
        return ResponseEntity.badRequest()
                .body(StandardResponse.error("bad credential", "incorrect email or password"));
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    protected ResponseEntity<?> handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        return ResponseEntity.badRequest()
                .body(StandardResponse.error("invalid idToken", "invalid Google idToken"));
    }

    @ExceptionHandler(AccountStatusException.class)
    protected ResponseEntity<?> handleAccountStatusException(AccountStatusException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(StandardResponse.error(ex.getMessage(), "account is locked or not verified"));
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<?> handleThrowable(Throwable ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        "No se ha podido procesar su solicitud. Contacte al administrdor."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
