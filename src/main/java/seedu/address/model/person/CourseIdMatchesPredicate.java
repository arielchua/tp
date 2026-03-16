package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code CourseId} matches the given course ID, case-insensitively.
 */
public class CourseIdMatchesPredicate implements Predicate<Person> {
    private final CourseId courseId;

    public CourseIdMatchesPredicate(CourseId courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean test(Person person) {
        return person.getCourseId().value.equalsIgnoreCase(courseId.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CourseIdMatchesPredicate)) {
            return false;
        }

        CourseIdMatchesPredicate otherCourseIdMatchesPredicate = (CourseIdMatchesPredicate) other;
        return courseId.equals(otherCourseIdMatchesPredicate.courseId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("courseId", courseId).toString();
    }
}
