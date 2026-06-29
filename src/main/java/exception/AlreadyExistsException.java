package exception;

public class AlreadyExistsException extends RuntimeException {

    // Можно назвать EntityAlreadyExistsException

    public AlreadyExistsException(String message) {
        super(message);
    }
    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}