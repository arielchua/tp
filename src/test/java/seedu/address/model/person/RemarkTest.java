package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    private static final String VALID_TEXT = "Good progress";
    private static final LocalDate VALID_DATE = LocalDate.of(2025, 10, 1);

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null, VALID_DATE));
        assertThrows(NullPointerException.class, () -> new Remark(VALID_TEXT, null));
    }

    @Test
    public void constructor_invalidText_throwsIllegalArgumentException() {
        // Text too long
        String longText = "a".repeat(Remark.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new Remark(longText, VALID_DATE));

        // Blank text
        assertThrows(IllegalArgumentException.class, () -> new Remark("   ", VALID_DATE));
    }

    @Test
    public void isValidText() {
        // EP: null text
        assertFalse(Remark.isValidText(null));

        // EP: Blank text (Invalid)
        assertFalse(Remark.isValidText("")); // empty string
        assertFalse(Remark.isValidText("  ")); // spaces only

        // EP: Length-based constraints (BV)
        assertTrue(Remark.isValidText("A")); // BV: Minimal valid length (1)
        assertTrue(Remark.isValidText("a".repeat(Remark.MAX_LENGTH))); // BV: Maximum valid length (100)
        assertFalse(Remark.isValidText("a".repeat(Remark.MAX_LENGTH + 1))); // BV: Exceeds maximum (101)

        // EP: Special characters (Unicode/Emoji)
        assertTrue(Remark.isValidText("Needs follow-up ⚠️"));

        // EP: Formatting (Trailing/Leading whitespace)
        // Note: This is valid as long as the total length including spaces is <= 100
        assertTrue(Remark.isValidText("  Good progress  "));
    }

    @Test
    public void equals() {
        Remark remark = new Remark(VALID_TEXT, VALID_DATE);

        // same values -> returns true
        assertEquals(new Remark(VALID_TEXT, VALID_DATE), remark);

        // same object -> returns true
        assertEquals(remark, remark);

        // null -> returns false
        assertNotEquals(null, remark);

        // different types -> returns false
        assertNotEquals(5.0f, remark);

        // different text -> returns false
        assertNotEquals(new Remark("Other text", VALID_DATE), remark);

        // different date -> returns false
        assertNotEquals(remark, new Remark(VALID_TEXT, VALID_DATE.plusDays(1)));
    }

    @Test
    public void hashCode_test() {
        Remark remark = new Remark(VALID_TEXT, VALID_DATE);
        assertEquals(remark.hashCode(), new Remark(VALID_TEXT, VALID_DATE).hashCode());
        assertNotEquals(remark.hashCode(), new Remark("Other", VALID_DATE).hashCode());
    }

    @Test
    public void toString_validRemark_returnsFormattedString() {
        Remark remark = new Remark("Met student", VALID_DATE);
        assertEquals("[2025-10-01] Met student", remark.toString());
    }
}
