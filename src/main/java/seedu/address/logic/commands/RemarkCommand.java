package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.time.LocalDate;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Adds remarks to a person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a remark for a person in the address book. "
            + "Parameters: "
            + "STUDENT INDEX (must be a valid index in the list) "
            + PREFIX_REMARK + "REMARK \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Excellent student who always submits assignments on time. \n";

    //replace Person with toString of person
    public static final String MESSAGE_ADD_REMARKS_SUCCESS = "Added remark to Person: \n%1$s";

    // public static final String MESSAGE_ADD_REMARKS_FAILURE = "Failed to add remark to Person: %1$s";

    private final Index targetIndex;
    private final Remark remark;

    /**
     * Creates a RemarksCommand to add remarks to a person by displayed index.
     *
     * @param targetIndex
     * @param remark
     */
    public RemarkCommand(Index targetIndex, Remark remark) {
        requireNonNull(targetIndex);
        requireNonNull(remark);
        this.targetIndex = targetIndex;
        this.remark = remark;
    }

    /**
     * Overloaded constructor for RemarkCommand.
     *
     * @param targetIndex
     * @param remark
     */
    public RemarkCommand(Index targetIndex, String remark) {
        this(targetIndex, new Remark(remark, LocalDate.now()));
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

        editedPerson.addRemark(remark);
        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_REMARKS_SUCCESS, Messages.format(editedPerson) + "\n"
                + "Remark: " + this.remark.getText()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand otherRemarkCommand = (RemarkCommand) other;
        return targetIndex.equals(otherRemarkCommand.targetIndex)
                && remark.equals(otherRemarkCommand.remark);
    }
}
