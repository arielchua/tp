package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's course ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCourseId(String)}
 */
public class CourseId {

    public static final String MESSAGE_CONSTRAINTS =
            "Course ID should follow the format used in the app, with no spaces"
            + " or special characters: e.g. CS2103.";
    public static final String VALIDATION_REGEX = "[A-Za-z0-9][A-Za-z0-9]*";
    public final String value;

    /**
     * Constructs a {@code CourseId}.
     *
     * @param courseId A valid course ID.
     */
    public CourseId(String courseId) {
        requireNonNull(courseId);
        checkArgument(isValidCourseId(courseId), MESSAGE_CONSTRAINTS);
        value = courseId.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid course ID.
     */
    public static boolean isValidCourseId(String test) {
        return test.matches(VALIDATION_REGEX);
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

        // instanceof handles nulls
        if (!(other instanceof CourseId)) {
            return false;
        }

        CourseId otherCourseId = (CourseId) other;
        return value.equals(otherCourseId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
