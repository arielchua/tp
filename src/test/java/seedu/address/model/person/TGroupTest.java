package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TGroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TGroup(null));
    }

    @Test
    public void constructor_invalidTGroup_throwsIllegalArgumentException() {
        String invalidTGroup = "T01!";
        assertThrows(IllegalArgumentException.class, () -> new TGroup(invalidTGroup));
    }

    @Test
    public void isValidTGroup() {
        // null tutorial group
        assertThrows(NullPointerException.class, () -> TGroup.isValidTGroup(null));

        // invalid tutorial groups
        assertFalse(TGroup.isValidTGroup("")); // empty string
        assertFalse(TGroup.isValidTGroup(" ")); // spaces only
        assertFalse(TGroup.isValidTGroup("T 01")); // contains spaces
        assertFalse(TGroup.isValidTGroup("T-01")); // contains hyphen
        assertFalse(TGroup.isValidTGroup("T_01")); // contains underscore
        assertFalse(TGroup.isValidTGroup("T01!")); // contains special character

        // valid tutorial groups
        assertTrue(TGroup.isValidTGroup("T01"));
        assertTrue(TGroup.isValidTGroup("t01"));
        assertTrue(TGroup.isValidTGroup("G1"));
        assertTrue(TGroup.isValidTGroup("123"));
    }

    @Test
    public void equals() {
        TGroup tGroup = new TGroup("T01");

        // same object -> returns true
        assertTrue(tGroup.equals(tGroup));

        // null -> returns false
        assertFalse(tGroup.equals(null));

        // different types -> returns false
        assertFalse(tGroup.equals(5.0f));

        // different values -> returns false
        assertFalse(tGroup.equals(new TGroup("T02")));

        // same values -> returns true
        assertTrue(tGroup.equals(new TGroup("T01")));
    }
}
