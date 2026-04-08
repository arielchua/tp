package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CancelCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    /**
     * Stores the person awaiting delete confirmation.
     */
    private Person pendingPersonToDelete;

    /**
     * Tracks whether the app is currently waiting for the user to confirm or cancel a delete operation.
     */
    private boolean isAwaitingDeleteConfirmation;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.addressBookParser = new AddressBookParser();
        this.pendingPersonToDelete = null;
        this.isAwaitingDeleteConfirmation = false;
    }

    /**
     * Returns true if the app is currently waiting for the user to confirm or cancel a pending delete.
     */
    private boolean hasPendingDeleteConfirmation() {
        return isAwaitingDeleteConfirmation && pendingPersonToDelete != null;
    }

    /**
     * Stores the given person as the pending person to delete and marks the app as waiting for confirmation.
     */
    private void setPendingDeleteConfirmation(Person person) {
        pendingPersonToDelete = person;
        isAwaitingDeleteConfirmation = true;
    }

    /**
     * Clears any pending delete confirmation state.
     */
    private void clearPendingDeleteConfirmation() {
        pendingPersonToDelete = null;
        isAwaitingDeleteConfirmation = false;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        String trimmedCommandText = commandText.trim();

        if (hasPendingDeleteConfirmation()) {
            if (trimmedCommandText.equalsIgnoreCase(ConfirmCommand.COMMAND_WORD)) {
                Person personToDelete = pendingPersonToDelete;
                model.deletePerson(personToDelete);
                clearPendingDeleteConfirmation();

                CommandResult commandResult = new CommandResult(
                        String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));

                try {
                    storage.saveAddressBook(model.getAddressBook());
                } catch (AccessDeniedException e) {
                    throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
                } catch (IOException ioe) {
                    throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
                }

                return commandResult;
            }

            if (trimmedCommandText.equalsIgnoreCase(CancelCommand.COMMAND_WORD)) {
                clearPendingDeleteConfirmation();
                return new CommandResult(CancelCommand.MESSAGE_CANCEL_SUCCESS);
            }

            throw new ParseException("Please type 'yes' to confirm deletion or 'no' to cancel.");
        }

        Command command = addressBookParser.parseCommand(commandText);

        if (command instanceof DeleteCommand) {
            DeleteCommand deleteCommand = (DeleteCommand) command;
            Person personToDelete = deleteCommand.getPersonToDelete(model);
            setPendingDeleteConfirmation(personToDelete);
            return new CommandResult(String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, personToDelete.getName()));
        }

        CommandResult commandResult = command.execute(model);

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
