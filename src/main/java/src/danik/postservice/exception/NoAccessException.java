package src.danik.postservice.exception;

public class NoAccessException extends RuntimeException {
    public NoAccessException(String message) {
        super(message);
    }
}
