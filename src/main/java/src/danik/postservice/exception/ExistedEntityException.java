package src.danik.postservice.exception;

public class ExistedEntityException extends RuntimeException {
    public ExistedEntityException(String message) {
        super(message);
    }
}
