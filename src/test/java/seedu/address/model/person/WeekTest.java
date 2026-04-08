package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WeekTest {

    @Test
    public void week_initialStatus_isDefault() {
        Week week = new Week(1);
        assertEquals("N", week.getStatus().toString());
        assertFalse(week.isAttended());
    }

    @Test
    public void week_markAsAttended_changesStatus() {
        Week week = new Week(1);
        week.markAsAttended();
        assertEquals("Y", week.getStatus().toString());
        assertTrue(week.isAttended());
    }

    @Test
    public void week_markAsAbsent_changesStatus() {
        Week week = new Week(1);
        week.markAsAbsent();
        assertEquals("A", week.getStatus().toString());
        assertFalse(week.isAttended());
    }

    @Test
    public void week_markAsDefault_resetsStatus() {
        Week week = new Week(1);
        week.markAsAttended();
        week.markAsDefault();
        assertEquals("N", week.getStatus().toString());
    }

    @Test
    public void markAsAttended_secondTime_throwsIllegalStateException() {
        Week week = new Week(1);
        week.markAsAttended();
        assertThrows(IllegalStateException.class, week::markAsAttended);
    }

    @Test
    public void markAsAbsent_secondTime_throwsIllegalStateException() {
        Week week = new Week(1);
        week.markAsAbsent();
        assertThrows(IllegalStateException.class, week::markAsAbsent);
    }

    @Test
    public void markAsDefault_secondTime_throwsIllegalStateException() {
        Week week = new Week(1);
        assertThrows(IllegalStateException.class, week::markAsDefault);
    }

    @Test
    public void week_equals_andToString() {
        Week week1 = new Week(1);
        Week week2 = new Week(1);
        assertEquals(week1, week2);
        week1.markAsAttended();
        week2.markAsAttended();
        assertEquals(week1, week2);
        assertEquals("W1: Y", week1.toString());
    }
    @Test
    public void markAsAttended_success() {
        Week w = new Week(1);
        w.markAsAttended();
        assertTrue(w.isAttended());
    }

    @Test
    public void markDuplicate_throwsException() {
        Week w = new Week(1);
        w.markAsAttended();

        assertThrows(IllegalStateException.class, w::markAsAttended);
    }

    @Test
    public void cancel_thenModify_throwsException() {
        Week w = new Week(1);
        w.markAsCancelled();

        assertThrows(IllegalStateException.class, w::markAsAttended);
    }

    @Test
    public void cancel_andUncancel_restoreStatus() {
        Week w = new Week(1);
        w.markAsAttended();
        w.markAsCancelled();
        w.markAsUncancelled();

        assertEquals(Week.Status.Y, w.getStatus());
    }

    @Test
    public void uncancel_notCancelled_throwsException() {
        Week w = new Week(1);

        assertThrows(IllegalStateException.class, w::markAsUncancelled);
    }
}
