package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

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

        assertTrue(list.getWeek(0).isAttended());
    }

    @Test
    public void markWeekAsAbsent_validIndex_success() {
        WeekList list = new WeekList();
        list.markWeekAsAttended(0);
        list.markWeekAsAbsent(0);

        assertFalse(list.getWeek(0).isAttended());
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

    @Test
    public void weekList_defaultConstruction_allWeeksDefault() {
        WeekList weekList = new WeekList();
        for (WeeklyAttendance week : weekList.getWeeks()) {
            assertEquals("N", week.getStatus().toString());
        }
    }

    @Test
    public void weekList_markWeekAsAttended_changesCorrectWeek() {
        WeekList weekList = new WeekList();
        weekList.markWeekAsAttended(2);
        assertEquals("Y", weekList.getWeek(2).getStatus().toString());
    }

    @Test
    public void weekList_markWeekAsAbsent_changesCorrectWeek() {
        WeekList weekList = new WeekList();
        weekList.markWeekAsAbsent(3);
        assertEquals("A", weekList.getWeek(3).getStatus().toString());
    }

    @Test
    public void weekList_markWeekAsDefault_changesCorrectWeek() {
        WeekList weekList = new WeekList();
        weekList.markWeekAsAttended(1);
        weekList.markWeekAsDefault(1);
        assertEquals("N", weekList.getWeek(1).getStatus().toString());
    }

    @Test
    public void weekList_copy_preservesAllStatuses() {
        WeekList original = new WeekList();
        original.markWeekAsAttended(0);
        original.markWeekAsAbsent(1);
        WeekList copy = original.copy();
        assertEquals("Y", copy.getWeek(0).getStatus().toString());
        assertEquals("A", copy.getWeek(1).getStatus().toString());
        assertEquals("N", copy.getWeek(2).getStatus().toString()); // default week
    }

    @Test
    public void weekList_isValidWeekList_acceptsValidString() {
        String input = "W1: Y W2: N W3: A W4: N W5: N W6: N W7: N W8: N W9: N W10: N W11: N W12: N W13: N";
        assertTrue(WeekList.isValidWeekList(input));
    }

    @Test
    public void weekList_isValidWeekList_rejectsInvalidString() {
        String input = "W1: Y W2: X W3: N"; // invalid status X and wrong length
        assertFalse(WeekList.isValidWeekList(input));
    }

    @Test
    public void weekList_buildWeekListFromString_createsCorrectWeekList() throws IllegalValueException {
        String input = "W1: Y W2: A W3: N W4: N W5: N W6: N W7: N W8: N W9: N W10: N W11: N W12: N W13: N";
        WeekList weekList = WeekList.buildWeekListFromString(input);
        assertEquals("Y", weekList.getWeek(0).getStatus().toString());
        assertEquals("A", weekList.getWeek(1).getStatus().toString());
        assertEquals("N", weekList.getWeek(2).getStatus().toString());
    }

    @Test
    public void weekList_calculateWeekAttendance_correctPercentage() {
        WeekList weekList = new WeekList();
        weekList.markWeekAsAttended(0);
        weekList.markWeekAsAttended(1);
        double expected = (2.0 / WeekList.NUMBER_OF_WEEKS) * 100;
        assertEquals(expected, weekList.calculateWeekAttendance());
    }

    @Test
    public void weekList_equalsAndCompareTo() {
        WeekList list1 = new WeekList();
        WeekList list2 = new WeekList();
        assertEquals(list1, list2);
        list1.markWeekAsAttended(0);
        assertNotEquals(list1, list2);
        assertTrue(list1.compareTo(list2) > 0);
    }

    @Test
    public void markWeek_success() {
        WeekList list = new WeekList();
        list.markWeekAsAttended(0);

        assertTrue(list.getWeek(0).isAttended());
    }

    @Test
    public void invalidIndex_throwsException() {
        WeekList list = new WeekList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.markWeekAsAttended(20));
    }


    @Test
    public void calculateAttendance_correct() {
        WeekList list = new WeekList();
        list.markWeekAsAttended(0);

        double result = list.calculateWeekAttendance();
        assertTrue(result > 0);
    }

    @Test
    public void cancelWeek_excludedFromAttendance() {
        WeekList list = new WeekList();
        list.markAsCancelled(0);

        double result = list.calculateWeekAttendance();
        assertEquals(0, result);
    }
}
