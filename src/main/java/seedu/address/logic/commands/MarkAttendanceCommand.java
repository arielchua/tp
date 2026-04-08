package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Week;
import seedu.address.model.person.WeekList;

/**
 * Marks the specified week (tutorial) as attended or not attended for a person.
 */
public class MarkAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "markattendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates attendance of a student.\n"
            + "Parameters: INDEX (positive integer) "
            + PREFIX_WEEK + "WEEK_NUMBER "
            + PREFIX_STATUS + "STATUS (Y = attended, A = absent, N = not marked)\n"
            + "Example: " + COMMAND_WORD + " 1 week/5 sta/Y";

    public static final String MESSAGE_INVALID_PERSON_INDEX =
            "The person index provided is invalid.";

    public static final String MESSAGE_WEEK_CANCELLED =
            "Week %1$d is cancelled and cannot be marked.";

    public static final String MESSAGE_DUPLICATE =
            "Week %1$d already has status '%2$s' for %3$s.";

    public static final String MESSAGE_SUCCESS =
            "Week %1$d marked as %2$s for: %3$s";


    public final Index index;
    public final Index weekNumber;
    public final Week.Status status;

    /**
     * Creates an MarkAttendanceCommand to update the attendance status
     * of a specific week for a student identified by their index.
     *
     * @param index Index of the student in the displayed person list.
     * @param weekNumber Index of the week to update (1-based index from user input).
     * @param status The attendance status to assign for the specified week.
     */
    public MarkAttendanceCommand(Index index, Index weekNumber, Week.Status status) {
        requireAllNonNull(index, weekNumber, status);
        this.index = index;
        this.weekNumber = weekNumber;
        this.status = status;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> persons = model.getFilteredPersonList();
        inputValidation(persons);
        Person personToEdit = persons.get(index.getZeroBased());

        WeekList updatedWeekList = personToEdit.getWeekList().copy();

        int weekIdx = weekNumber.getZeroBased();
        weekCancelValidation(updatedWeekList, weekIdx);
        try {
            applyStatusUpdate(updatedWeekList, weekIdx);
        } catch (IllegalStateException e) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE,
                            weekNumber.getOneBased(),
                            status,
                            formatPerson(personToEdit)));
        }
        Person editedPerson = createEditedPerson(personToEdit, updatedWeekList);

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                        weekNumber.getOneBased(),
                        status,
                        formatPerson(personToEdit))
        );
    }

    /**
     * Applies the correct attendance update based on status.
     */
    private void applyStatusUpdate(WeekList list, int index) throws CommandException {
        switch (status) {
        case Y:
            list.markWeekAsAttended(index);
            break;
        case A:
            list.markWeekAsAbsent(index);
            break;
        case N:
            list.markWeekAsDefault(index);
            break;
        default:
            throw new CommandException("Invalid attendance status.");
        }
    }

    /**
     * Creates a new Person with updated WeekList.
     */
    private Person createEditedPerson(Person original, WeekList updatedWeekList) {
        return new Person(
                original.getName(),
                original.getCourseId(),
                original.getEmail(),
                original.getStudentId(),
                original.getTGroup(),
                original.getTele(),
                updatedWeekList,
                original.getProgress()
        );
    }

    /**
     * Checks if week number is within valid bounds.
     */
    private boolean isValidWeek(Index week) {
        int zeroBased = week.getZeroBased();
        return zeroBased >= 0 && zeroBased < WeekList.NUMBER_OF_WEEKS;
    }

    private void weekCancelValidation(WeekList updatedWeekList, int weekIdx) throws CommandException {
        Week week = (Week) updatedWeekList.getWeek(weekIdx);
        if (week.isCancelled()) {
            throw new CommandException(
                    String.format(MESSAGE_WEEK_CANCELLED, weekNumber.getOneBased()));
        }
    }

    private void inputValidation(List<Person> persons) throws CommandException {
        if (index.getZeroBased() >= persons.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
        }
        if (!isValidWeek(weekNumber)) {
            throw new CommandException(WeekList.MESSAGE_INVALID_WEEK);
        }
    }

    private String formatPerson(Person person) {
        return person.getName() + " (" + person.getStudentId() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkAttendanceCommand)) {
            return false;
        }
        MarkAttendanceCommand otherCommand = (MarkAttendanceCommand) other;
        return index.equals(otherCommand.index)
                && weekNumber.equals(otherCommand.weekNumber)
                && status == otherCommand.status;
    }
}
