package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's student ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS = "Student ID should be a 9 characters long, alphanumeric";
    public static final String VALIDATION_REGEX = "[A-Za-z][0-9]{7}[A-Za-z]";
    public final String value;

    /**
     * Constructs a {@code StudentId}.
     *
     * @param value A valid student ID.
     */
    public StudentId(String value) {
        requireNonNull(value);
        checkArgument(isValidStudentId(value), MESSAGE_CONSTRAINTS);
        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && studentId.matches(VALIDATION_REGEX); // Example: 9-digit number
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StudentId)) {
            return false;
        }

        StudentId otherStudentId = (StudentId) other;
        return value.equals(otherStudentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
