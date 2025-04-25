package tech.ixirsii.parse.exception;

/**
 * Exception when an argument can't be parsed.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public class InvalidParameterException extends Exception {
    /**
     * Constructor.
     *
     * @param message Error message.
     */
    public InvalidParameterException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message Error message.
     * @param cause   Error cause.
     */
    public InvalidParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
