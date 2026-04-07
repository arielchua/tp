package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} matches the given filter criteria.
 * All present fields must match (logical AND). Missing fields are ignored.
 */
public class FilterMatchesPredicate implements Predicate<Person> {
    private final Optional<CourseId> courseId;
    private final Optional<TGroup> tGroup;
    private final Optional<Progress> progress;
    private final Optional<Integer> absenceCount;
    /**
     * Creates a {@code FilterMatchesPredicate} with the given filter criteria.
     * Each criterion is treated as optional; a missing value (empty Optional)
     * implies that the filter for that specific field is disabled.
     *
     * @param courseId The course identifier to match.
     * @param tGroup The tutorial group identifier to match.
     * @param progress The student progress status to match.
     * @param absenceCount The minimum threshold of absences to filter by.
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

        boolean matchesCourse = courseId.isEmpty() || person.getCourseId().equals(courseId.get());
        boolean matchesTGroup = tGroup.isEmpty() || person.getTGroup().equals(tGroup.get());
        boolean matchesProgress = progress.isEmpty() || person.getProgress().equals(progress.get());

        // Absence filter identifies students with count at or above the specified threshold
        boolean matchesAbsenceCount = absenceCount.isEmpty() || person.getAbsenceCount() >= absenceCount.get();

        return matchesCourse && matchesTGroup && matchesProgress && matchesAbsenceCount;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterMatchesPredicate otherPredicate)) {
            return false;
        }

        return courseId.equals(otherPredicate.courseId)
                && tGroup.equals(otherPredicate.tGroup)
                && progress.equals(otherPredicate.progress)
                && absenceCount.equals(otherPredicate.absenceCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, tGroup, progress, absenceCount);
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
