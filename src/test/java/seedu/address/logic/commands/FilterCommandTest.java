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
    public void toStringMethod() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()), Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
