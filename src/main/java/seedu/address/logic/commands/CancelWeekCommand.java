package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.WeekList;

/**
 * Marks the specified week (tutorial) as Cancelled for the same (CourseId-Tutorial) pair
 */
public class CancelWeekCommand extends Command {

    public static final String COMMAND_WORD = "cancelweek";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels the week from attendance table.\n"
            + "Parameters: [crs/COURSE_ID] [tg/TUTORIAL_ID] "
            + PREFIX_WEEK + "WEEK_NUMBER\n"
            + "All parameters must be included\n"
            + "Example: " + COMMAND_WORD + " crs/CS2103T tg/T01 week/5";

    public static final String MESSAGE_DUPLICATE =
            "Week %1$d is already cancelled for course %2$s tutorial %3$s.";

    public static final String MESSAGE_COURSE_TUT_INVALID =
            "Course %s with tutorial %s does not exist and cannot be cancelled.";

    public static final String MESSAGE_SUCCESS =
            "Week %1$d cancelled for course %2$s tutorial %3$s.";

    private final CourseId courseId;
    private final TGroup tGroup;
    private final Index weekNumber;

    /**
     * Creates a {@code CancelWeekCommand} to mark a specific week as cancelled
     * for all students belonging to the given course and tutorial group.
     *
     * @param courseId The course identifier of the students whose attendance week will be cancelled.
     * @param tGroup The tutorial group within the specified course.
     * @param weekNumber The week number to be cancelled (1-based index).
     * @throws NullPointerException if any of the arguments are null.
     */
    public CancelWeekCommand(CourseId courseId, TGroup tGroup, Index weekNumber) {
        requireAllNonNull(courseId, tGroup, weekNumber);
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.weekNumber = weekNumber;
    }
    /**
     * Executes the cancel week command.
     * Validates the input parameters and cancels the specified week for all students
     * in the given course and tutorial group.
     *
     * @param model The model containing the data of the application.
     * @return A {@code CommandResult} containing a success message.
     * @throws CommandException If:
     *     <ul>
     *     <li>The specified course and tutorial group does not exist.</li>
     *     <li>The week number is invalid (out of range).</li>
     *     <li>The week is already cancelled for the given course and tutorial group.</li>
     *     </ul>
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        int weekIdx = weekNumber.getZeroBased();
        parsedInputValidation(model, weekIdx);
        model.addCancelledWeek(courseId, tGroup, weekIdx);
        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                weekNumber.getOneBased(),
                courseId,
                tGroup));
    }

    private void parsedInputValidation(Model model, int weekIdx) throws CommandException {
        if (!model.hasCourseTGroup(courseId, tGroup)) {
            throw new CommandException(
                    String.format(MESSAGE_COURSE_TUT_INVALID,
                            courseId, tGroup));
        }
        if (!isValidWeek()) {
            throw new CommandException(WeekList.MESSAGE_INVALID_WEEK);
        }
        if (model.isWeekCancelled(courseId, tGroup, weekIdx)) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE,
                    weekNumber.getOneBased(),
                    courseId,
                    tGroup));
        }
    }

    /**
     * Checks if week index is within valid bounds.
     */
    private boolean isValidWeek() {
        int zeroBased = weekNumber.getZeroBased();
        return zeroBased >= 0 && zeroBased < WeekList.NUMBER_OF_WEEKS;
    }

    /**
     * Compares this {@code CancelWeekCommand} with another object for equality.
     *
     * @param other The object to compare with.
     * @return {@code true} if both commands have the same courseId, tutorial group,
     *         and week number; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CancelWeekCommand)) {
            return false;
        }
        CancelWeekCommand otherCommand = (CancelWeekCommand) other;
        return courseId.equals(otherCommand.courseId)
                && tGroup.equals(otherCommand.tGroup)
                && weekNumber.equals(otherCommand.weekNumber);
    }
}
