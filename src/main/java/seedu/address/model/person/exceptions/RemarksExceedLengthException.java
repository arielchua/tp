package seedu.address.model.person.exceptions;

/**
 * Signals that the operation will result in remarks exceeding the maximum allowed length.
 * Maximum allowed length is 100 characters.
 */
public class RemarksExceedLengthException extends RuntimeException {
    public RemarksExceedLengthException(String message) {
        super(message);
    }
}
