package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class FilterMatchesPredicateTest {

    @Test
    public void equals() {
        Optional<CourseId> courseId = Optional.of(new CourseId("CS2103T"));
        Optional<TGroup> tGroup = Optional.of(new TGroup("T01"));
        Optional<Progress> progress = Optional.of(Progress.ON_TRACK);
        Optional<Integer> absenceCount = Optional.of(2);

        FilterMatchesPredicate predicate = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);

        // same object -> returns true
        assertTrue(predicate.equals(predicate));

        // same values -> returns true
        FilterMatchesPredicate predicateCopy = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);
        assertTrue(predicate.equals(predicateCopy));

        // different types -> returns false
        assertFalse(predicate.equals(1));

        // null -> returns false
        assertFalse(predicate.equals(null));

        // different course ID -> returns false
        FilterMatchesPredicate differentCourse = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2101")), tGroup, progress, absenceCount);
        assertFalse(predicate.equals(differentCourse));

        // different tutorial group -> returns false
        FilterMatchesPredicate differentTGroup = new FilterMatchesPredicate(
                courseId, Optional.of(new TGroup("T05")), progress, absenceCount);
        assertFalse(predicate.equals(differentTGroup));

        // different progress -> returns false
        FilterMatchesPredicate differentProgress = new FilterMatchesPredicate(
                courseId, tGroup, Optional.of(Progress.AT_RISK), absenceCount);
        assertFalse(predicate.equals(differentProgress));

        // different absence count -> returns false
        FilterMatchesPredicate differentAbsence = new FilterMatchesPredicate(
                courseId, tGroup, progress, Optional.of(3));
        assertFalse(predicate.equals(differentAbsence));
    }

    @Test
    public void test_courseIdMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T").build()));
        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withCourseId("cs2103t").build()));
        assertTrue(predicate.test(new PersonBuilder().withCourseId("Cs2103T").build()));
    }

    @Test
    public void test_courseIdMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS2101").build()));
    }

    @Test
    public void test_tGroupMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withTGroup("T01").build()));
        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withTGroup("t01").build()));
    }

    @Test
    public void test_progressMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withProgress(Progress.ON_TRACK).build()));
    }

    @Test
    public void test_absenceCount_thresholdLogic() {
        // Filter threshold: Absences >= 3
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(3));

        // 1. Person with exactly 3 absences -> returns true
        assertTrue(predicate.test(new PersonBuilder().withAbsences(3).build()));

        // 2. Person with 5 absences (above threshold) -> returns true
        assertTrue(predicate.test(new PersonBuilder().withAbsences(5).build()));

        // 3. Person with 2 absences (below threshold) -> returns false
        assertFalse(predicate.test(new PersonBuilder().withAbsences(2).build()));

        // 4. Person with 0 absences (default) -> returns false
        assertFalse(predicate.test(new PersonBuilder().build()));

        // 5. Filter: Absences >= 0 -> returns true for any person
        FilterMatchesPredicate zeroPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        assertTrue(zeroPredicate.test(new PersonBuilder().withAbsences(0).build()));
        assertTrue(zeroPredicate.test(new PersonBuilder().withAbsences(10).build()));
    }

    @Test
    public void test_allFieldsMatch_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(2));

        // All fields match exactly or satisfy threshold
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(2)
                .build()));

        // All match, and absences (5) is above threshold (2)
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("cs2103t")
                .withTGroup("t01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(5)
                .build()));
    }

    @Test
    public void test_multiFieldMismatch_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(3));

        // Everything matches except course
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS1231")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(3)
                .build()));

        // Everything matches except absences (too few)
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(2)
                .build()));

        // Everything matches except progress
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.NEEDS_ATTENTION)
                .withAbsences(3)
                .build()));
    }

    @Test
    public void test_emptyPredicate_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        // Empty predicate should match any person
        assertTrue(predicate.test(new PersonBuilder().build()));
        assertTrue(predicate.test(new PersonBuilder().withAbsences(10).withProgress(Progress.AT_RISK).build()));
    }

    @Test
    public void test_null_throwsNullPointerException() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        // Ensure the predicate handles null person gracefully by throwing NPE as per requireNonNull
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> predicate.test(null));
    }

    @Test
    public void test_absenceCount_maxBoundary() {
        // Filter threshold: Absences >= 13 (The maximum possible)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(13));

        // Person with 12 absences -> FALSE
        assertFalse(predicate.test(new PersonBuilder().withAbsences(12).build()));

        // Person with 13 absences -> TRUE
        assertTrue(predicate.test(new PersonBuilder().withAbsences(13).build()));
    }

    @Test
    public void test_mixedMatches_returnsFalse() {
        // Predicate with all fields set
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(2));

        // 1. Only CourseId matches -> FALSE
        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS2103T")
                .withTGroup("T02").withProgress(Progress.AT_RISK).withAbsences(0).build()));

        // 2. Only TGroup matches -> FALSE
        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS1010")
                .withTGroup("T01").withProgress(Progress.AT_RISK).withAbsences(0).build()));

        // 3. Only Progress matches -> FALSE
        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS1010")
                .withTGroup("T02").withProgress(Progress.ON_TRACK).withAbsences(0).build()));

        // 4. Only AbsenceCount matches -> FALSE
        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS1010")
                .withTGroup("T02").withProgress(Progress.AT_RISK).withAbsences(2).build()));
    }

    @Test
    public void test_absenceCountHighThreshold_returnsFalse() {
        // Threshold higher than possible weeks (e.g., 14)
        // Even with max absences (13), this should always be false.
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(14));

        assertFalse(predicate.test(new PersonBuilder().withAbsences(13).build()));
    }

    @Test
    public void test_toString() {
        Optional<CourseId> courseId = Optional.of(new CourseId("CS2103T"));
        Optional<TGroup> tGroup = Optional.of(new TGroup("T01"));
        Optional<Progress> progress = Optional.of(Progress.ON_TRACK);
        Optional<Integer> absenceCount = Optional.of(2);

        FilterMatchesPredicate predicate = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);

        String expected = FilterMatchesPredicate.class.getCanonicalName() + "{courseId=" + courseId
                + ", tGroup=" + tGroup + ", progress=" + progress + ", absenceCount=" + absenceCount
                + "}";
        assertEquals(expected, predicate.toString());
    }
}
