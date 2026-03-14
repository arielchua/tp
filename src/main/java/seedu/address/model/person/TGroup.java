package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's tutorial group in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTGroup(String)}
 */
public class TGroup {

    public static final String MESSAGE_CONSTRAINTS =
            "Tutorial group should only contain alphanumeric characters "
            + "and should not be blank";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";
    public final String value;

    /**
     * Constructs a {@code TGroup}.
     *
     * @param tGroup A valid tutorial group.
     */
    public TGroup(String tGroup) {
        requireNonNull(tGroup);
        checkArgument(isValidTGroup(tGroup), MESSAGE_CONSTRAINTS);
        value = tGroup;
    }

    /**
     * Returns true if a given string is a valid tutorial group.
     */
    public static boolean isValidTGroup(String test) {
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
        if (!(other instanceof TGroup)) {
            return false;
        }

        TGroup otherTGroup = (TGroup) other;
        return value.equals(otherTGroup.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
