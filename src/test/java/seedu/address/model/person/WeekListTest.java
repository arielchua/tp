package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class WeekListTest {

    @Test
    public void constructor_default_allNotAttended() {
        WeekList list = new WeekList();

        for (WeeklyAttendance week : list.getWeeks()) {
            assertFalse(week.isAttended());
        }
    }

    @Test
    public void markWeekAsAttended_validIndex_success() {
        WeekList list = new WeekList();
        list.markWeekAsAttended(0);

        assertTrue(list.getWeeks()[0].isAttended());
    }

    @Test
    public void markWeekAsAbsent_validIndex_success() {
        WeekList list = new WeekList();
        list.markWeekAsAttended(0);
        list.markWeekAsAbsent(0);

        assertFalse(list.getWeeks()[0].isAttended());
    }

    @Test
    public void equals_sameContent_true() {
        WeekList list1 = new WeekList();
        WeekList list2 = new WeekList();

        assertEquals(list1, list2);
    }

    @Test
    public void equals_differentContent_false() {
        WeekList list1 = new WeekList();
        WeekList list2 = new WeekList();

        list1.markWeekAsAttended(0);

        assertNotEquals(list1, list2);
    }

    @Test
    public void copy_createsDeepCopy() {
        WeekList original = new WeekList();
        original.markWeekAsAttended(0);

        WeekList copy = original.copy();

        assertEquals(original, copy);

        // Modify original, copy should not change
        original.markWeekAsAbsent(0);

        assertNotEquals(original, copy);
    }

    @Test
    public void calculateAttendance_correctPercentage() {
        WeekList list = new WeekList();

        list.markWeekAsAttended(0);
        list.markWeekAsAttended(1);

        double expected = (2.0 / WeekList.NUMBER_OF_WEEKS) * 100;
        assertEquals(expected, list.calculateWeekAttendance());
    }

    @Test
    public void compareTo_higherAttendance_returnsPositive() {
        WeekList list1 = new WeekList();
        WeekList list2 = new WeekList();

        list1.markWeekAsAttended(0);

        assertTrue(list1.compareTo(list2) > 0);
    }

    @Test
    public void getLabels_correctLabels() {
        WeekList list = new WeekList();
        List<String> labels = list.getLabels();

        assertEquals("W1", labels.get(0));
        assertEquals("W10", labels.get(9));
        assertEquals(WeekList.NUMBER_OF_WEEKS, labels.size());
    }

    @Test
    public void isValidWeekList_validInput_true() {
        String valid = "W1: Y W2: N W3: A W4: Y W5: N W6: Y W7: N W8: Y W9: N W10: Y W11: Y W12: Y W13: Y";
        assertTrue(WeekList.isValidWeekList(valid));
    }

    @Test
    public void isValidWeekList_invalidLabel_false() {
        String invalid = "L1: Y W2: N W3: A W4: Y W5: N W6: Y W7: N W8: Y W9: N W10: Y";
        assertFalse(WeekList.isValidWeekList(invalid));
    }

    @Test
    public void isValidWeekList_invalidStatus_false() {
        String invalid = "W1: X W2: N W3: A W4: Y W5: N W6: Y W7: N W8: Y W9: N W10: Y";
        assertFalse(WeekList.isValidWeekList(invalid));
    }

    @Test
    public void isValidWeekList_wrongLength_false() {
        String invalid = "W1: Y W2: N";
        assertFalse(WeekList.isValidWeekList(invalid));
    }

    @Test
    public void toString_containsAllWeeks() {
        WeekList list = new WeekList();
        String result = list.toString();

        assertTrue(result.contains("W1"));
        assertTrue(result.contains("W10"));
    }
}
