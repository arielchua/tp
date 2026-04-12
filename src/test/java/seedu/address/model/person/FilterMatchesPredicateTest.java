package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for {@link FilterMatchesPredicate}.
 * Focuses on Equivalence Partitioning (EP) and Boundary Value Analysis (BVA).
 */
public class FilterMatchesPredicateTest {

    @Test
    public void equals() {
        Optional<CourseId> courseId = Optional.of(new CourseId("CS2103T"));
        Optional<TGroup> tGroup = Optional.of(new TGroup("T01"));
        Optional<Progress> progress = Optional.of(Progress.ON_TRACK);
        Optional<Integer> absenceCount = Optional.of(2);

        FilterMatchesPredicate predicate = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);

        // same object -> returns true
        assertEquals(predicate, predicate);

        // same values -> returns true
        FilterMatchesPredicate predicateCopy = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);
        assertEquals(predicate, predicateCopy);

        // different types -> returns false
        assertNotEquals(1, predicate);

        // null -> returns false
        assertNotEquals(null, predicate);

        // different course ID -> returns false
        FilterMatchesPredicate differentCourse = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2101")), tGroup, progress, absenceCount);
        assertNotEquals(predicate, differentCourse);

        // different tutorial group -> returns false
        FilterMatchesPredicate differentTGroup = new FilterMatchesPredicate(
                courseId, Optional.of(new TGroup("T05")), progress, absenceCount);
        assertNotEquals(predicate, differentTGroup);

        // different progress -> returns false
        FilterMatchesPredicate differentProgress = new FilterMatchesPredicate(
                courseId, tGroup, Optional.of(Progress.AT_RISK), absenceCount);
        assertNotEquals(predicate, differentProgress);

        // different absence count -> returns false
        FilterMatchesPredicate differentAbsence = new FilterMatchesPredicate(
                courseId, tGroup, progress, Optional.of(3));
        assertNotEquals(predicate, differentAbsence);
    }

    @Test
    public void equals_normalizedCourseIdAndTGroup_areEqualAndHashCodeEqual() {
        // EP: Case-insensitive normalization (Input vs Model)
        FilterMatchesPredicate upperCoursePredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());
        FilterMatchesPredicate lowerCoursePredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("cs2103t")), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(upperCoursePredicate, lowerCoursePredicate);
        assertEquals(upperCoursePredicate.hashCode(), lowerCoursePredicate.hashCode());

        FilterMatchesPredicate upperTGroupPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty(), Optional.empty());
        FilterMatchesPredicate lowerTGroupPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("t01")), Optional.empty(), Optional.empty());

        assertEquals(upperTGroupPredicate, lowerTGroupPredicate);
        assertEquals(upperTGroupPredicate.hashCode(), lowerTGroupPredicate.hashCode());
    }

    @Test
    public void test_courseIdMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T").build()));
        // EP: Case-insensitive match
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
        // EP: Case-insensitive match
        assertTrue(predicate.test(new PersonBuilder().withTGroup("t01").build()));
    }

    @Test
    public void test_progressMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withProgress(Progress.ON_TRACK).build()));
    }

    /**
     * Tests the threshold logic for absence counts using Equivalence Partitioning and Boundary Value Analysis.
     * EP: [0, threshold-1] -> false
     * EP: [threshold, max_absences] -> true
     */
    @Test
    public void test_absenceCount_thresholdLogic() {
        // Filter threshold: Absences >= 3
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(3));

        // BVA: Exactly at threshold
        assertTrue(predicate.test(new PersonBuilder().withAbsences(3).build()));

        // EP: Above threshold
        assertTrue(predicate.test(new PersonBuilder().withAbsences(5).build()));

        // BVA: Just below threshold
        assertFalse(predicate.test(new PersonBuilder().withAbsences(2).build()));

        // EP: Minimum possible value (0)
        assertFalse(predicate.test(new PersonBuilder().withAbsences(0).build()));

        // BVA: Minimum threshold check (Absences >= 0)
        FilterMatchesPredicate zeroPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        assertTrue(zeroPredicate.test(new PersonBuilder().withAbsences(0).build()));
        assertTrue(zeroPredicate.test(new PersonBuilder().withAbsences(10).build()));
    }

    @Test
    public void test_partialPredicate_ignoresMissingFields() {
        // EP: Only CourseId is set; others are Optional.empty()
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.empty(), Optional.empty(), Optional.empty());

        // Matches despite different progress and absences
        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T")
                .withProgress(Progress.AT_RISK).withAbsences(10).build()));
        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T")
                .withProgress(Progress.ON_TRACK).withAbsences(0).build()));
    }

    @Test
    public void test_allFieldsMatch_returnsTrue() {
        // EP: All fields provided (Logical AND)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(2));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(2)
                .build()));
    }

    @Test
    public void test_multiFieldMismatch_returnsFalse() {
        // EP: Multiple conditions met but one fails (AND logic)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(3));

        // Course mismatch
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS1231")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(3).build()));

        // Absence mismatch (below threshold)
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .withAbsences(2).build()));

        // All fields mismatch
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("XX9999")
                .withTGroup("G99")
                .withProgress(Progress.AT_RISK)
                .withAbsences(0).build()));
    }

    @Test
    public void test_emptyPredicate_returnsTrue() {
        // EP: No filters provided
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_null_throwsNullPointerException() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        assertThrows(NullPointerException.class, () -> predicate.test(null));
    }

    @Test
    public void test_singleFieldMismatch_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());

        // Correct everything except course
        assertFalse(predicate.test(new PersonBuilder().withCourseId("MA1521").build()));
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

    @Test
    public void equals_caseInsensitiveMatch_returnsTrue() {
        FilterMatchesPredicate predicate1 = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(), Optional.empty());
        FilterMatchesPredicate predicate2 = new FilterMatchesPredicate(
                Optional.of(new CourseId("cs2103t")), Optional.empty(), Optional.empty(), Optional.empty());
        assertEquals(predicate1, predicate2);
    }

}
