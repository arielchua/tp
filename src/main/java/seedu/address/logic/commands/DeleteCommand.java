package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes a student identified using its displayed index and name from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the student identified by the index number "
            + "and exact name used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer), STUDENT_NAME (must be an exact match) \n"
            + "Example: " + COMMAND_WORD + " 1" + " or " + COMMAND_WORD + " John Doe";

    public static final String MESSAGE_DELETE_STUDENT_SUCCESS = "Deleted Student: %1$s";

    public static final String MESSAGE_PERSON_NOT_FOUND_BY_NAME =
            "No student with the name \"%1$s\" found on the list.";

    private final Index targetIndex;
    private final Name targetName;

    /**
     * Creates a DeleteCommand to delete a student by displayed index.
     *
     * @param targetIndex Index of the student in the displayed student list.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /**
     * Creates a DeleteCommand to delete a student by students name.
     *
     * @param targetName Name of the student to delete.
     */
    public DeleteCommand(Name targetName) {
        this.targetIndex = null;
        this.targetName = targetName;
    }

    /**
     * Deletes a student from the address book, identified either by the displayed list index
     * or by an exact full name match in the currently displayed student list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person studentToDelete;

        if (targetIndex != null) { // select by index
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            studentToDelete = lastShownList.get(targetIndex.getZeroBased());
        } else { // select by name
            if (targetName == null) {
                throw new CommandException("Please provide a full student name.");
            }
            studentToDelete = lastShownList.stream()
                    .filter(p -> p.getName().equals(targetName))
                    .findFirst()
                    .orElse(null);
            if (studentToDelete == null) {
                throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND_BY_NAME, targetName.toString()));
            }
        }

        model.deletePerson(studentToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_STUDENT_SUCCESS, Messages.format(studentToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return Objects.equals(targetIndex, otherDeleteCommand.targetIndex)
                && Objects.equals(targetName, otherDeleteCommand.targetName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .toString();
    }
}
