package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_nullText_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null, LocalDate.of(2025, 10, 1)));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark("Needs follow-up", null));
    }

    @Test
    public void constructor_textTooLong_throwsIllegalArgumentException() {
        String longText = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> new Remark(longText, LocalDate.of(2025, 10, 1)));
    }

    @Test
    public void isValidText() {
        assertFalse(Remark.isValidText(null));
        assertTrue(Remark.isValidText(""));
        assertTrue(Remark.isValidText("Good progress"));
        assertTrue(Remark.isValidText("a".repeat(100)));
        assertFalse(Remark.isValidText("a".repeat(101)));
    }

    @Test
    public void constructor_validRemark_success() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        Remark remark = new Remark("Consulted student about milestone", date);

        assertEquals("Consulted student about milestone", remark.getText());
        assertEquals(date, remark.getDate());
    }

    @Test
    public void equals() {
        Remark first = new Remark("Good participation", LocalDate.of(2025, 10, 1));
        Remark same = new Remark("Good participation", LocalDate.of(2025, 10, 1));
        Remark differentText = new Remark("Needs follow-up", LocalDate.of(2025, 10, 1));
        Remark differentDate = new Remark("Good participation", LocalDate.of(2025, 10, 2));

        assertEquals(first, first);
        assertEquals(first, same);
        assertNotEquals(first, differentText);
        assertNotEquals(first, differentDate);
        assertNotEquals(first, null);
        assertNotEquals(first, 1);
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Remark first = new Remark("Good participation", LocalDate.of(2025, 10, 1));
        Remark same = new Remark("Good participation", LocalDate.of(2025, 10, 1));

        assertEquals(first.hashCode(), same.hashCode());
    }

    @Test
    public void toString_validRemark_returnsFormattedString() {
        Remark remark = new Remark("Met during consultation", LocalDate.of(2025, 10, 1));
        assertEquals("[2025-10-01] Met during consultation", remark.toString());
    }
}
