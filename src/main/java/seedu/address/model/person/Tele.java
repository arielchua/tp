package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Telegram handle in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTele(String)}
 */
public class Tele {
    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handle should only contain alphanumeric characters and underscores, "
            + "with an optional leading '@', and should not be blank";
    public static final String VALIDATION_REGEX = "@?[A-Za-z0-9_]+";
    public final String value;

    /**
     * Constructs a {@code Tele}.
     *
     * @param tele A valid Telegram handle.
     */
    public Tele(String tele) {
        requireNonNull(tele);
        checkArgument(isValidTele(tele), MESSAGE_CONSTRAINTS);
        value = tele;
    }

    /**
     * Returns true if a given string is a valid Telegram handle.
     */
    public static boolean isValidTele(String test) {
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
        if (!(other instanceof Tele)) {
            return false;
        }

        Tele otherTele = (Tele) other;
        return value.equals(otherTele.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
