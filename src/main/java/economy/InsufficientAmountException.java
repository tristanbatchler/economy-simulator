package economy;

/**
 * Thrown to indicate that an agent has tried to do something that requires a specific amount, but does not meet such
 * requirements.
 *
 * @author Tristan Batchler
 */
public class InsufficientAmountException extends Exception {
    public InsufficientAmountException(String message) {
        super(message);
    }
}
