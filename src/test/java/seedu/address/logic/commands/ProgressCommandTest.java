package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ProgressCommand.
 */
public class ProgressCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updatedPerson = new PersonBuilder(personToUpdate)
                .withProgress(Progress.ON_TRACK)
                .build();

        ProgressCommand progressCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.ON_TRACK);

        String expectedMessage = String.format(
                ProgressCommand.MESSAGE_UPDATE_PROGRESS_SUCCESS,
                Messages.format(updatedPerson),
                updatedPerson.getProgress());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToUpdate, updatedPerson);

        assertCommandSuccess(progressCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updatedPerson = new PersonBuilder(personToUpdate)
                .withProgress(Progress.AT_RISK)
                .build();

        ProgressCommand progressCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.AT_RISK);

        String expectedMessage = String.format(
                ProgressCommand.MESSAGE_UPDATE_PROGRESS_SUCCESS,
                Messages.format(updatedPerson),
                updatedPerson.getProgress());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToUpdate, updatedPerson);

        assertCommandSuccess(progressCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearProgressByIndex_success() {
        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithProgress = new PersonBuilder(personToUpdate)
                .withProgress(Progress.ON_TRACK)
                .build();
        model.setPerson(personToUpdate, personWithProgress);

        Person clearedPerson = new PersonBuilder(personWithProgress)
                .withProgress(Progress.NOT_SET)
                .build();

        ProgressCommand progressCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.NOT_SET);

        String expectedMessage = String.format(
                ProgressCommand.MESSAGE_CLEAR_PROGRESS_SUCCESS,
                Messages.format(clearedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithProgress, clearedPerson);

        assertCommandSuccess(progressCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ProgressCommand progressCommand = new ProgressCommand(outOfBoundIndex, Progress.ON_TRACK);

        assertCommandFailure(progressCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ProgressCommand progressCommand = new ProgressCommand(outOfBoundIndex, Progress.ON_TRACK);

        assertCommandFailure(progressCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_overwriteExistingProgress_success() {
        Person personToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithProgress = new PersonBuilder(personToUpdate)
                .withProgress(Progress.ON_TRACK)
                .build();
        model.setPerson(personToUpdate, personWithProgress);

        Person updatedPerson = new PersonBuilder(personWithProgress)
                .withProgress(Progress.AT_RISK)
                .build();

        ProgressCommand progressCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.AT_RISK);

        String expectedMessage = String.format(
                ProgressCommand.MESSAGE_UPDATE_PROGRESS_SUCCESS,
                Messages.format(updatedPerson),
                updatedPerson.getProgress());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithProgress, updatedPerson);

        assertCommandSuccess(progressCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        ProgressCommand firstCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.ON_TRACK);
        ProgressCommand firstCommandCopy = new ProgressCommand(INDEX_FIRST_PERSON, Progress.ON_TRACK);
        ProgressCommand secondCommand = new ProgressCommand(INDEX_SECOND_PERSON, Progress.ON_TRACK);
        ProgressCommand differentProgressCommand = new ProgressCommand(INDEX_FIRST_PERSON, Progress.AT_RISK);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(differentProgressCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
    }
}
