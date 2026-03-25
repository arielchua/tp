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
        Optional<CourseId> firstCourseId = Optional.of(new CourseId("CS2103T"));
        Optional<CourseId> secondCourseId = Optional.of(new CourseId("CS2101"));
        Optional<TGroup> firstTGroup = Optional.of(new TGroup("T01"));
        Optional<Progress> firstProgress = Optional.of(Progress.ON_TRACK);
        Optional<Progress> secondProgress = Optional.of(Progress.AT_RISK);
        Optional<Integer> firstAbsenceCount = Optional.of(2);

        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(firstCourseId, Optional.empty(),
                Optional.empty(), Optional.empty());
        FilterMatchesPredicate secondPredicate = new FilterMatchesPredicate(secondCourseId, Optional.empty(),
                Optional.empty(), Optional.empty());
        FilterMatchesPredicate thirdPredicate = new FilterMatchesPredicate(firstCourseId, firstTGroup,
                Optional.empty(), Optional.empty());
        FilterMatchesPredicate fourthPredicate = new FilterMatchesPredicate(firstCourseId, Optional.empty(),
                firstProgress, Optional.empty());
        FilterMatchesPredicate fifthPredicate = new FilterMatchesPredicate(firstCourseId, Optional.empty(),
                secondProgress, Optional.empty());
        FilterMatchesPredicate sixthPredicate = new FilterMatchesPredicate(firstCourseId, Optional.empty(),
                Optional.empty(), firstAbsenceCount);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        FilterMatchesPredicate firstPredicateCopy = new FilterMatchesPredicate(firstCourseId, Optional.empty(),
                Optional.empty(), Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different course ID -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different combination (course + tgroup vs course only) -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));

        // different combination (course + progress vs course only) -> returns false
        assertFalse(firstPredicate.equals(fourthPredicate));

        // different progress -> returns false
        assertFalse(fourthPredicate.equals(fifthPredicate));

        // different absence count -> returns false
        assertFalse(firstPredicate.equals(sixthPredicate));
    }

    @Test
    public void test_courseIdMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(),
                Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T").build()));

        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withCourseId("cs2103t").build()));
        assertTrue(predicate.test(new PersonBuilder().withCourseId("Cs2103T").build()));
    }

    @Test
    public void test_courseIdMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(),
                Optional.empty());

        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS2101").build()));
        assertFalse(predicate.test(new PersonBuilder().withCourseId("MA2001").build()));
    }

    @Test
    public void test_tGroupMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withTGroup("T01").build()));

        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withTGroup("t01").build()));
        assertTrue(predicate.test(new PersonBuilder().withTGroup("T01").build()));
    }

    @Test
    public void test_tGroupMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty(), Optional.empty());

        assertFalse(predicate.test(new PersonBuilder().withTGroup("T02").build()));
        assertFalse(predicate.test(new PersonBuilder().withTGroup("G01").build()));
    }

    @Test
    public void test_progressMatches_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withProgress(Progress.valueOf("ON_TRACK")).build()));
    }

    @Test
    public void test_progressMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK), Optional.empty());

        assertFalse(predicate.test(new PersonBuilder().withProgress(Progress.valueOf("AT_RISK")).build()));
        assertFalse(predicate.test(new PersonBuilder().withProgress(Progress.valueOf("NEEDS_ATTENTION")).build()));
    }

    @Test
    public void test_bothCourseAndTGroupMatch_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.empty(),
                Optional.empty());

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("cs2103t")
                .withTGroup("t01")
                .build()));
    }

    @Test
    public void test_bothCourseAndTGroupMatch_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.empty(),
                Optional.empty());

        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T02")
                .build()));

        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2101")
                .withTGroup("T01")
                .build()));

        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("G05")
                .build()));
    }

    @Test
    public void test_allThreeFiltersMatch_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.empty());

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));
    }

    @Test
    public void test_allThreeFiltersMatch_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.empty());

        // progress mismatch
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("AT_RISK"))
                .build()));

        // tGroup mismatch
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T02")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));

        // course mismatch
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2101")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));
    }

    @Test
    public void test_courseIdOnlyFilter_ignoresTGroupAndProgress() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty(),
                Optional.empty());

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T99")
                .withProgress(Progress.valueOf("AT_RISK"))
                .build()));
    }

    @Test
    public void test_tGroupOnlyFilter_ignoresCourseIdAndProgress() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("AT_RISK"))
                .build()));
    }

    @Test
    public void test_progressOnlyFilter_ignoresCourseIdAndTGroup() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("G05")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build()));
    }

    @Test
    public void test_absenceCountMatches_returnsTrue() {
        // abs/0 matches a person with 0 absences (placeholder value from getAbsenceCount())
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));

        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_absenceCountMatches_returnsFalse() {
        // abs/1 does not match a person with 0 absences (placeholder value from getAbsenceCount())
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(1));

        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_absenceOnlyFilter_ignoresCourseIdTGroupAndProgress() {
        // abs/0 matches regardless of course, group, and progress
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("G05")
                .withProgress(Progress.AT_RISK)
                .build()));
    }

    @Test
    public void test_allFourFiltersMatch_returnsTrue() {
        // abs/0 matches person with 0 absences (placeholder)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(0));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .build()));
    }

    @Test
    public void test_allFourFiltersMatch_returnsFalse() {
        // abs/1 does not match person with 0 absences (placeholder)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK),
                Optional.of(1));

        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.ON_TRACK)
                .build()));
    }

    @Test
    public void test_toString() {
        Optional<CourseId> courseId = Optional.of(new CourseId("CS2103T"));
        Optional<TGroup> tGroup = Optional.of(new TGroup("T01"));
        Optional<Progress> progress = Optional.of(Progress.ON_TRACK);
        Optional<Integer> absenceCount = Optional.empty();

        FilterMatchesPredicate predicate = new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount);

        String expected = "seedu.address.model.person.FilterMatchesPredicate{courseId=" + courseId
                + ", tGroup=" + tGroup + ", progress=" + progress + ", absenceCount=" + absenceCount
                + "}";
        assertEquals(expected, predicate.toString());
    }
}
