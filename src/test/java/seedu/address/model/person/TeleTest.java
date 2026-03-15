package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TeleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tele(null));
    }

    @Test
    public void constructor_invalidTele_throwsIllegalArgumentException() {
        String invalidTele = "alex-tan";
        assertThrows(IllegalArgumentException.class, () -> new Tele(invalidTele));
    }

    @Test
    public void isValidTele() {
        // null telegram handle
        assertThrows(NullPointerException.class, () -> Tele.isValidTele(null));

        // invalid telegram handles
        assertFalse(Tele.isValidTele("")); // empty string
        assertFalse(Tele.isValidTele(" ")); // spaces only
        assertFalse(Tele.isValidTele("alex tan")); // contains spaces
        assertFalse(Tele.isValidTele("alex-tan")); // contains hyphen
        assertFalse(Tele.isValidTele("alex!tan")); // contains special character
        assertFalse(Tele.isValidTele("alex/tan")); // contains slash

        // valid telegram handles
        assertTrue(Tele.isValidTele("alextan"));
        assertTrue(Tele.isValidTele("alex_tan"));
        assertTrue(Tele.isValidTele("@alextan"));
        assertTrue(Tele.isValidTele("alex.tan"));
        assertTrue(Tele.isValidTele("alex123"));
    }

    @Test
    public void equals() {
        Tele tele = new Tele("alextan");

        // same values -> returns true
        assertTrue(tele.equals(new Tele("alextan")));

        // same object -> returns true
        assertTrue(tele.equals(tele));

        // null -> returns false
        assertFalse(tele.equals(null));

        // different types -> returns false
        assertFalse(tele.equals(5.0f));

        // different values -> returns false
        assertFalse(tele.equals(new Tele("alex_tan")));
    }
}
