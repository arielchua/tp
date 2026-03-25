package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} matches the given filter criteria.
 * A person matches if all present filter fields match:
 * <ul>
 *     <li>course ID matches case-insensitively</li>
 *     <li>tutorial group matches case-insensitively</li>
 *     <li>progress matches exactly</li>
 *     <li>absence count is greater than or equal to the given threshold</li>
 * </ul>
 */
public class FilterMatchesPredicate implements Predicate<Person> {
    private final Optional<CourseId> courseId;
    private final Optional<TGroup> tGroup;
    private final Optional<Progress> progress;
    private final Optional<Integer> absenceCount;

    /**
     * Creates a {@code FilterMatchesPredicate} with the given filter criteria.
     *
     * @param courseId The course ID to match, or empty if not filtering by course ID.
     * @param tGroup The tutorial group to match, or empty if not filtering by tutorial group.
     * @param progress The progress to match, or empty if not filtering by progress.
     * @param absenceCount The minimum absence count to match, or empty if not filtering by absences.
     */
    public FilterMatchesPredicate(Optional<CourseId> courseId, Optional<TGroup> tGroup,
                                  Optional<Progress> progress, Optional<Integer> absenceCount) {
        requireNonNull(courseId);
        requireNonNull(tGroup);
        requireNonNull(progress);
        requireNonNull(absenceCount);

        this.courseId = courseId;
        this.tGroup = tGroup;
        this.progress = progress;
        this.absenceCount = absenceCount;
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);

        boolean matchesCourse =
                courseId.isEmpty() || person.getCourseId().value.equalsIgnoreCase(courseId.get().value);
        boolean matchesTGroup =
                tGroup.isEmpty() || person.getTGroup().value.equalsIgnoreCase(tGroup.get().value);
        boolean matchesProgress =
                progress.isEmpty() || person.getProgress().equals(progress.get());
        boolean matchesAbsenceCount =
                absenceCount.isEmpty() || person.getAbsenceCount() >= absenceCount.get();

        return matchesCourse && matchesTGroup && matchesProgress && matchesAbsenceCount;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterMatchesPredicate)) {
            return false;
        }

        FilterMatchesPredicate otherPredicate = (FilterMatchesPredicate) other;
        return courseId.equals(otherPredicate.courseId)
                && tGroup.equals(otherPredicate.tGroup)
                && progress.equals(otherPredicate.progress)
                && absenceCount.equals(otherPredicate.absenceCount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("courseId", courseId)
                .add("tGroup", tGroup)
                .add("progress", progress)
                .add("absenceCount", absenceCount)
                .toString();
    }
}
