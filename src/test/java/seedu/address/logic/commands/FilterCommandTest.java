package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FilterMatchesPredicate;

/**
 * Integration tests for {@code FilterCommand} checking interaction with the Model.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterCommand(null));
    }

    @Test
    public void execute_filterByCourse_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByTGroup_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(),
                Optional.of(BENSON.getTGroup()),
                Optional.empty(), Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByProgress_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(),
                Optional.of(CARL.getProgress()),
                Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByAbsenceCount_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(HOON.getAbsenceCount()));
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByAbsenceCount_noMatches() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(100)); // Threshold likely higher than any typical student

        assertFilterCommandSuccess(predicate);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_filterByMultipleFields_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(),
                Optional.of(ALICE.getAbsenceCount()));
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void equals() {
        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterMatchesPredicate secondPredicate = new FilterMatchesPredicate(
                Optional.of(BENSON.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        assertEquals(filterFirstCommand, filterFirstCommand);
        assertEquals(filterFirstCommand, new FilterCommand(firstPredicate));

        assertNotEquals(1, filterFirstCommand);
        assertNotEquals(null, filterFirstCommand);
        assertNotEquals(filterFirstCommand, filterSecondCommand);
    }

    @Test
    public void toStringMethod() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);

        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * Executes a {@code FilterCommand} with the given {@code predicate},
     * and checks that the result matches the expected model state.
     */
    private void assertFilterCommandSuccess(FilterMatchesPredicate predicate) {
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }
}
