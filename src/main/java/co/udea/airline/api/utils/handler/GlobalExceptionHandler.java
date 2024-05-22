package co.udea.airline.api.utils.handler;

import co.udea.airline.api.utils.common.StandardResponse;
import co.udea.airline.api.utils.exception.BusinessException;
import co.udea.airline.api.utils.exception.DataNotFoundException;
import co.udea.airline.api.utils.exception.DataDuplicatedException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<?> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataDuplicatedException.class)
    protected ResponseEntity<?> handleDataDuplicatedException(DataDuplicatedException ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(new StandardResponse<>(400, "Validation error", errors),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
                return new ResponseEntity<>(new StandardResponse<>(400, "Validation error", ex.getMessage()),
                        HttpStatus.BAD_REQUEST);
        }
        

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<?> handleThrowable(Throwable ex) {
        return new ResponseEntity<>(
                new StandardResponse<>(StandardResponse.StatusStandardResponse.ERROR,
                        "No se ha podido procesar su solicitud. Contacte al administrdor."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
