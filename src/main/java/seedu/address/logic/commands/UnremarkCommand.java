package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNREMARK;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Deletes a remark from a person in the address book.
 */
public class UnremarkCommand extends Command {

    public static final String COMMAND_WORD = "unremark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deleted a remark for a person in the address book. "
            + "Parameters: "
            + "STUDENT INDEX (must be a valid index in the list) "
            + PREFIX_UNREMARK + "REMARK_INDEX (must be a positive integer) \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_UNREMARK + "1 \n";

    public static final String MESSAGE_DELETE_REMARKS_SUCCESS = "Deleted remark from Person: \n%1$s";

    public static final String MESSAGE_INVALID_REMARK_INDEX = "The remark index provided is invalid.";

    private final Index targetIndex;
    private final Index remarkIndex;

    /**
     * Creates an UnremarkCommand to delete a remark from a person by displayed index.
     *
     * @param targetIndex Index of the person in the filtered person list.
     * @param remarkIndex Index of the remark to delete.
     */
    public UnremarkCommand(Index targetIndex, Index remarkIndex) {
        requireNonNull(targetIndex);
        requireNonNull(remarkIndex);
        this.targetIndex = targetIndex;
        this.remarkIndex = remarkIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // check if the index is valid
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        Person editedPerson = new Person(personToEdit);

        // check if the remark index is valid
        if (remarkIndex.getZeroBased() >= personToEdit.getRemarks().size()) {
            throw new CommandException(MESSAGE_INVALID_REMARK_INDEX);
        }

        Remark deletedRemark = personToEdit.getRemarks().get(remarkIndex.getZeroBased());
        editedPerson.deleteRemark(remarkIndex);
        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_REMARKS_SUCCESS, Messages.format(editedPerson)) + "\n"
                + "Deleted Remark: " + deletedRemark.getText());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnremarkCommand otherCommand)) {
            return false;
        }
        return targetIndex.equals(otherCommand.targetIndex)
                && remarkIndex.equals(otherCommand.remarkIndex);
    }
}
