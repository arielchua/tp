package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        // Uses the data defined in TypicalPersons
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_filterByCourse_success() {
        // Alice is in CS2103T
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());

        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_filterByTGroup_success() {
        // Benson is in T09
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(),
                Optional.of(BENSON.getTGroup()),
                Optional.empty(), Optional.empty());

        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_filterByProgress_success() {
        // Carl is NEEDS_ATTENTION
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(),
                Optional.of(CARL.getProgress()),
                Optional.empty());

        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_noMatches_success() {
        // Use a CourseId that definitely doesn't exist in TypicalPersons
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("EMPTY999")),
                Optional.empty(), Optional.empty(), Optional.empty());

        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, 0),
                expectedModel);
    }

    @Test
    public void equals() {
        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()), Optional.empty(), Optional.empty(), Optional.empty());
        FilterMatchesPredicate secondPredicate = new FilterMatchesPredicate(
                Optional.of(BENSON.getCourseId()), Optional.empty(), Optional.empty(), Optional.empty());

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }
    @Test
    public void execute_filterByAbsenceCount_success() {
        // Assume Alice has 0 absences and we filter for >= 0
        FilterMatchesPredicate zeroPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));

        FilterCommand zeroCommand = new FilterCommand(zeroPredicate);
        expectedModel.updateFilteredPersonList(zeroPredicate);
        assertCommandSuccess(zeroCommand, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);

        // Filter for a high absence count that likely matches no one in TypicalPersons
        FilterMatchesPredicate highAbsencePredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(10));

        FilterCommand highAbsenceCommand = new FilterCommand(highAbsencePredicate);
        expectedModel.updateFilteredPersonList(highAbsencePredicate);
        assertCommandSuccess(highAbsenceCommand, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, 0),
                expectedModel);
    }

    @Test
    public void execute_filterByMultipleFields_success() {
        // Filter by both CourseId and Absence Count
        // Alice: CourseId=CS2103T, Absences=0 (assumed)
        FilterMatchesPredicate multiPredicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(),
                Optional.empty(),
                Optional.of(0));

        FilterCommand command = new FilterCommand(multiPredicate);
        expectedModel.updateFilteredPersonList(multiPredicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);

        // Ensure Alice is actually in the filtered results
        assertTrue(expectedModel.getFilteredPersonList().contains(ALICE));
    }

    @Test
    public void execute_allFieldsEmpty_success() {
        // Empty predicate should show everyone (matching your FilterMatchesPredicate logic)
        FilterMatchesPredicate emptyPredicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        FilterCommand command = new FilterCommand(emptyPredicate);
        expectedModel.updateFilteredPersonList(emptyPredicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);

        assertEquals(model.getAddressBook().getPersonList().size(),
                expectedModel.getFilteredPersonList().size());
    }
    @Test
    public void toStringMethod() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()), Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
