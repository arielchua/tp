package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PersonTest {

    private final Name validName = new Name("Alex Tan");
    private final CourseId validCourseId = new CourseId("CS2030S");
    private final Email validEmail = new Email("alextan@u.nus.edu");
    private final StudentId validStudentId = new StudentId("A1234567X");
    private final TGroup validTGroup = new TGroup("T01");
    private final Tele validTele = new Tele("alextan");

    private final Person person = new Person(
            validName, validCourseId, validEmail, validStudentId, validTGroup, validTele);

    /**
     * Tests if any input is null.
     * Assuming all fields compulsory in Person constructor.
     */
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Person(null, validCourseId, validEmail, validStudentId, validTGroup, validTele));
        assertThrows(NullPointerException.class, () ->
                new Person(validName, null, validEmail, validStudentId, validTGroup, validTele));
        assertThrows(NullPointerException.class, () ->
                new Person(validName, validCourseId, null, validStudentId, validTGroup, validTele));
        assertThrows(NullPointerException.class, () ->
                new Person(validName, validCourseId, validEmail, null, validTGroup, validTele));
        assertThrows(NullPointerException.class, () ->
                new Person(validName, validCourseId, validEmail, validStudentId, null, validTele));
        assertThrows(NullPointerException.class, () ->
                new Person(validName, validCourseId, validEmail, validStudentId, validTGroup, null));
    }

    /**
     * Checks if 2 people are the same person if they have the same studentId.
     */
    @Test
    public void isSamePerson() {
        Person person = new Person(
            new Name("Alex Tan"),
            new CourseId("CS2030S"),
            new Email("alextan@u.nus.edu"),
            new StudentId("A1234567X"),
            new TGroup("T01"),
            new Tele("alextan"));

        // same object -> returns true
        assertTrue(person.isSamePerson(person));

        // null -> returns false
        assertFalse(person.isSamePerson(null));

        // same studentId, all other attributes different -> returns true
        Person editedPerson = new Person(
                new Name("Bob Lim"),
                new CourseId("CS2040S"),
                new Email("boblim@u.nus.edu"),
                new StudentId("A1234567X"),
                new TGroup("T02"),
                new Tele("boblim"));
        assertTrue(person.isSamePerson(editedPerson));

        // different studentId, all other attributes same -> returns false
        editedPerson = new Person(
                new Name("Alex Tan"),
                new CourseId("CS2030S"),
                new Email("alextan@u.nus.edu"),
                new StudentId("B1234567X"),
                new TGroup("T01"),
                new Tele("alextan"));
        assertFalse(person.isSamePerson(editedPerson));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person samePerson = new Person(
                new Name("Alex Tan"),
                new CourseId("CS2030S"),
                new Email("alextan@u.nus.edu"),
                new StudentId("A1234567X"),
                new TGroup("T01"),
                new Tele("alextan"));
        assertTrue(person.equals(samePerson));

        // same object -> returns true
        assertTrue(person.equals(person));

        // null -> returns false
        assertFalse(person.equals(null));

        // different type -> returns false
        assertFalse(person.equals(5));

        // different email -> returns false
        Person editedPerson = new Person(
                validName,
                validCourseId,
                new Email("other@u.nus.edu"),
                validStudentId,
                validTGroup,
                validTele);
        assertFalse(person.equals(editedPerson));

        // different studentId -> returns false
        editedPerson = new Person(
                validName,
                validCourseId,
                validEmail,
                new StudentId("B1234567X"),
                validTGroup,
                validTele);
        assertFalse(person.equals(editedPerson));

        // different tele -> returns false
        editedPerson = new Person(
                validName,
                validCourseId,
                validEmail,
                validStudentId,
                validTGroup,
                new Tele("otheruser"));
        assertFalse(person.equals(editedPerson));

        // different name, same email/studentId/tele -> returns true
        editedPerson = new Person(
                new Name("Bob Tan"),
                validCourseId,
                validEmail,
                validStudentId,
                validTGroup,
                validTele);
        assertTrue(person.equals(editedPerson));

        // different courseId, same email/studentId/tele -> returns true
        editedPerson = new Person(
                validName,
                new CourseId("CS2040S"),
                validEmail,
                validStudentId,
                validTGroup,
                validTele);
        assertTrue(person.equals(editedPerson));

        // different tGroup, same email/studentId/tele -> returns true
        editedPerson = new Person(
                validName,
                validCourseId,
                validEmail,
                validStudentId,
                new TGroup("T02"),
                validTele);
        assertTrue(person.equals(editedPerson));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{name=" + person.getName()
                + ", courseId=" + person.getCourseId()
                + ", email=" + person.getEmail()
                + ", studentId=" + person.getStudentId()
                + ", tGroup=" + person.getTGroup()
                + ", tele=" + (person.getTele() == null ? "-" : person.getTele()) + "}";
        assertEquals(expected, person.toString());
    }
}
