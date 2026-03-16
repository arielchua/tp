package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CourseIdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CourseId(null));
    }

    @Test
    public void constructor_invalidCourseId_throwsIllegalArgumentException() {
        String invalidCourseId = "CS2030S!";
        assertThrows(IllegalArgumentException.class, () -> new CourseId(invalidCourseId));
    }

    @Test
    public void isValidCourseId() {
        // null course ID
        assertThrows(NullPointerException.class, () -> CourseId.isValidCourseId(null));

        // invalid course IDs
        assertFalse(CourseId.isValidCourseId("")); // empty string
        assertFalse(CourseId.isValidCourseId(" ")); // spaces only (must start with letter/digit)
        assertFalse(CourseId.isValidCourseId("CS2030S!")); // special character not allowed
        assertFalse(CourseId.isValidCourseId("CS2030_S")); // underscore not allowed

        // valid course IDs
        assertTrue(CourseId.isValidCourseId("CS2030S"));
        assertTrue(CourseId.isValidCourseId("cs2030s"));
        assertTrue(CourseId.isValidCourseId("12345"));
        assertTrue(CourseId.isValidCourseId("A1B2C3"));
        assertTrue(CourseId.isValidCourseId("CS 2030S")); // space allowed
        assertTrue(CourseId.isValidCourseId("CS2030-S")); // hyphen allowed
        assertTrue(CourseId.isValidCourseId("CS2030/S")); // slash allowed
        assertTrue(CourseId.isValidCourseId("CS2030:S")); // colon allowed
    }

    @Test
    public void equals() {
        CourseId courseId = new CourseId("CS2030S");

        // same values -> returns true
        assertTrue(courseId.equals(new CourseId("CS2030S")));

        // same object -> returns true
        assertTrue(courseId.equals(courseId));

        // null -> returns false
        assertFalse(courseId.equals(null));

        // different types -> returns false
        assertFalse(courseId.equals(5.0f));

        // different values -> returns false
        assertFalse(courseId.equals(new CourseId("CS2040S")));
    }
}
