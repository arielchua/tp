package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ViewCommand.
 */
public class ViewCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewCommand(null));
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        assertViewCommandSuccess(INDEX_FIRST_PERSON, model);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertViewCommandSuccess(INDEX_FIRST_PERSON, model);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ViewCommand viewCommand = new ViewCommand(outOfBoundsIndex);
        assertCommandFailure(viewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundsIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getPersonList().size());
        ViewCommand viewCommand = new ViewCommand(outOfBoundsIndex);
        assertCommandFailure(viewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Model emptyModel = new ModelManager();
        ViewCommand viewCommand = new ViewCommand(INDEX_FIRST_PERSON);
        assertCommandFailure(viewCommand, emptyModel, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ViewCommand viewFirstCommand = new ViewCommand(INDEX_FIRST_PERSON);
        ViewCommand viewSecondCommand = new ViewCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertEquals(viewFirstCommand, viewFirstCommand);

        // same values -> returns true
        ViewCommand viewFirstCommandCopy = new ViewCommand(INDEX_FIRST_PERSON);
        assertEquals(viewFirstCommand, viewFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, viewFirstCommand);

        // null -> returns false
        assertNotEquals(null, viewFirstCommand);

        // different person -> returns false
        assertNotEquals(viewFirstCommand, viewSecondCommand);
    }

    @Test
    public void toStringMethod() {
        ViewCommand viewCommand = new ViewCommand(INDEX_FIRST_PERSON);
        String expected = ViewCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON + "}";
        assertEquals(expected, viewCommand.toString());
    }

    /**
     * Asserts that a {@code ViewCommand} with the given {@code index} executes successfully on the {@code model}.
     * Verifies UI flags, feedback message, and model state.
     */
    private void assertViewCommandSuccess(Index index, Model model) {
        ViewCommand viewCommand = new ViewCommand(index);
        Person personToView = model.getFilteredPersonList().get(index.getZeroBased());

        // --- Capture pre-execution state ---
        List<Person> initialList = List.copyOf(model.getFilteredPersonList());

        try {
            CommandResult result = viewCommand.execute(model);

            // --- Verify feedback and UI flags ---
            assertTrue(result.getFeedbackToUser().contains(personToView.getName().fullName));
            assertEquals(personToView, result.getPersonToView());
            assertTrue(result.shouldShowView());
            assertFalse(result.isExit());
            assertFalse(result.isHelpRequest());

            // --- Verify model state was not mutated ---
            assertEquals(initialList, model.getFilteredPersonList());

        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }
}
