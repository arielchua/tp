package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.Remark;

/**
 * Updates the progress status of a student.
 */
public class ProgressCommand extends Command {

    public static final String COMMAND_WORD = "updateprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the progress of a student.\n"
            + "Progress values:\n"
            + "  on_track - student is doing well\n"
            + "  needs_attention - student may need closer monitoring\n"
            + "  at_risk - student requires urgent attention\n"
            + "  clear - removes the progress status\n"
            + "Format: " + COMMAND_WORD + " INDEX p/PROGRESS\n"
            + "Example: " + COMMAND_WORD + " 1 p/on_track";

    public static final String MESSAGE_UPDATE_PROGRESS_SUCCESS = "Updated progress for student: %1$s.\n"
            + "New progress: %2$s";
    public static final String MESSAGE_CLEAR_PROGRESS_SUCCESS = "Cleared progress for student: %1$s";

    private final Index index;
    private final Progress progress;

    /**
     * Creates a ProgressCommand using index mode.
     */
    public ProgressCommand(Index index, Progress progress) {
        requireNonNull(index);
        requireNonNull(progress);

        this.index = index;
        this.progress = progress;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, progress);

        model.setPerson(personToEdit, editedPerson);

        if (progress == Progress.NOT_SET) {
            return new CommandResult(String.format(
                    MESSAGE_CLEAR_PROGRESS_SUCCESS, Messages.format(editedPerson)));
        }

        return new CommandResult(String.format(
                MESSAGE_UPDATE_PROGRESS_SUCCESS, Messages.format(editedPerson), editedPerson.getProgress()));
    }

    /**
     * Returns a person with the same fields as {@code personToEdit}, but with updated progress.
     */
    private static Person createEditedPerson(Person personToEdit, Progress updatedProgress) {
        assert personToEdit != null;

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getCourseId(),
                personToEdit.getEmail(),
                personToEdit.getStudentId(),
                personToEdit.getTGroup(),
                personToEdit.getTele(),
                personToEdit.getWeekList(),
                updatedProgress);
        for (Remark remark : personToEdit.getRemarks()) {
            editedPerson.addRemark(remark);
        }
        return editedPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ProgressCommand)) {
            return false;
        }

        ProgressCommand otherCommand = (ProgressCommand) other;
        return index.equals(otherCommand.index)
                && progress.equals(otherCommand.progress);
    }
}
