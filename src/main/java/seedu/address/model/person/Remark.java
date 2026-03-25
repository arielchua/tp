package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a Remark attached to a Person.
 * Guarantees: immutable; fields are present and valid.
 */
public class Remark {

    public static final String MESSAGE_TEXT_CONSTRAINTS =
            "Remark text must not be null and must not exceed 100 characters.";

    public static final int MAX_LENGTH = 100;

    private final String text;
    private final LocalDate date;

    /**
     * Constructs a {@code Remark}.
     *
     * @param text The remark text.
     * @param date The remark date.
     */
    public Remark(String text, LocalDate date) {
        requireNonNull(text);
        requireNonNull(date);
        checkArgument(isValidText(text), MESSAGE_TEXT_CONSTRAINTS);
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns true if the remark text is valid.
     */
    public static boolean isValidText(String text) {
        return text != null && text.length() <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + text;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Remark)) {
            return false;
        }

        Remark otherRemark = (Remark) other;
        return text.equals(otherRemark.text)
                && date.equals(otherRemark.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, date);
    }
}
