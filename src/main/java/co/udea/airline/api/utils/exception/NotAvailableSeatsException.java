package co.udea.airline.api.utils.exception;

public class NotAvailableSeatsException extends GeneralRuntimeException{

    private static final long serialVersionUID = 1L;

    public NotAvailableSeatsException(String message) {
        super(message);
    }

    public NotAvailableSeatsException(String message, String translationKey) {
        super(message, translationKey);
    }
}
