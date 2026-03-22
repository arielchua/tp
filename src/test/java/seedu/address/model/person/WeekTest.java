package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WeekTest {

    @Test
    public void constructor_defaultStatus_isN() {
        Week week = new Week(1);

        assertEquals(1, week.getWeek());
        assertFalse(week.isAttended());
        assertEquals("N", week.getStatus());
    }

    @Test
    public void markAsAttended_fromDefault_success() {
        Week week = new Week(1);

        week.markAsAttended();

        assertTrue(week.isAttended());
        assertEquals("Y", week.getStatus());
    }

    @Test
    public void markAsAbsent_fromDefault_success() {
        Week week = new Week(1);

        week.markAsAbsent();

        assertFalse(week.isAttended());
        assertEquals("A", week.getStatus());
    }

    @Test
    public void markAsAttended_twice_throwsException() {
        Week week = new Week(1);

        week.markAsAttended();

        assertThrows(IllegalStateException.class, week::markAsAttended);
    }

    @Test
    public void markAsAbsent_twice_throwsException() {
        Week week = new Week(1);

        week.markAsAbsent();

        assertThrows(IllegalStateException.class, week::markAsAbsent);
    }

    @Test
    public void markAsAttended_thenAbsent_success() {
        Week week = new Week(1);

        week.markAsAttended();
        week.markAsAbsent();

        assertFalse(week.isAttended());
        assertEquals("A", week.getStatus());
    }

    @Test
    public void markAsAbsent_thenAttended_success() {
        Week week = new Week(1);

        week.markAsAbsent();
        week.markAsAttended();

        assertTrue(week.isAttended());
        assertEquals("Y", week.getStatus());
    }

    @Test
    public void equals_sameObject_true() {
        Week week = new Week(1);
        assertEquals(week, week);
    }

    @Test
    public void equals_sameValues_true() {
        Week w1 = new Week(1);
        Week w2 = new Week(1);

        assertEquals(w1, w2);
    }

    @Test
    public void equals_differentWeekNumber_false() {
        Week w1 = new Week(1);
        Week w2 = new Week(2);

        assertNotEquals(w1, w2);
    }

    @Test
    public void equals_differentStatus_false() {
        Week w1 = new Week(1);
        Week w2 = new Week(1);

        w1.markAsAttended();

        assertNotEquals(w1, w2);
    }

    @Test
    public void equals_absentVsDefault_false() {
        Week w1 = new Week(1);
        Week w2 = new Week(1);

        w1.markAsAbsent(); // A
        // w2 is still N

        assertNotEquals(w1, w2);
    }

    @Test
    public void toString_correctFormat() {
        Week week = new Week(3);

        assertEquals("W3: N", week.toString());

        week.markAsAttended();
        assertEquals("W3: Y", week.toString());

        week.markAsAbsent();
        assertEquals("W3: A", week.toString());
    }
}
