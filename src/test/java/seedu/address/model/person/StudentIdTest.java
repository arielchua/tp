package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StudentIdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudentId(null));
    }

    @Test
    public void constructor_invalidStudentId_throwsIllegalArgumentException() {
        String invalidStudentId = "A123";
        assertThrows(IllegalArgumentException.class, () -> new StudentId(invalidStudentId));
    }

    @Test
    public void isValidStudentId() {
        // null student ID
        assertFalse(StudentId.isValidStudentId(null));

        // invalid student IDs
        assertFalse(StudentId.isValidStudentId("")); // empty string
        assertFalse(StudentId.isValidStudentId(" ")); // spaces only
        assertFalse(StudentId.isValidStudentId("A1234567")); // 8 characters
        assertFalse(StudentId.isValidStudentId("A123456789")); // 10 characters
        assertFalse(StudentId.isValidStudentId("A1234567!")); // special character
        assertFalse(StudentId.isValidStudentId("A123 456X")); // contains space
        assertFalse(StudentId.isValidStudentId("A123-456X")); // contains hyphen

        // valid student IDs
        assertTrue(StudentId.isValidStudentId("A1234567X"));
        assertTrue(StudentId.isValidStudentId("a1234567x"));
        assertTrue(StudentId.isValidStudentId("123456789"));
    }

    @Test
    public void equals() {
        StudentId studentId = new StudentId("A1234567X");

        // same values -> returns true
        assertTrue(studentId.equals(new StudentId("A1234567X")));

        // same object -> returns true
        assertTrue(studentId.equals(studentId));

        // null -> returns false
        assertFalse(studentId.equals(null));

        // different types -> returns false
        assertFalse(studentId.equals(5.0f));

        // different values -> returns false
        assertFalse(studentId.equals(new StudentId("B1234567X")));
    }
}
