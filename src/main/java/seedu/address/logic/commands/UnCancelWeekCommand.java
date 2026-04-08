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
 * Marks the specified week (tutorial) as uncancelled for a given CourseId–Tutorial group pair.
 */
public class UnCancelWeekCommand extends Command {

    public static final String COMMAND_WORD = "uncancelweek";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Uncancels the week from attendance table.\n"
            + "Parameters: [crs/COURSE_ID] [tg/TUTORIAL_ID] "
            + PREFIX_WEEK + "WEEK_NUMBER\n"
            + "All parameters must be included\n"
            + "Example: " + COMMAND_WORD + " crs/CS2103T tg/T01 week/5";
    public static final String MESSAGE_NOT_CANCELLED =
            "Week %1$d is not cancelled for course %2$s tutorial %3$s.";
    public static final String MESSAGE_COURSE_TUT_INVALID =
            "Course %s with tutorial %s does not exist and cannot be uncancelled.";
    public static final String MESSAGE_SUCCESS =
            "Week %1$d uncancelled for course %2$s tutorial %3$s.";

    private final CourseId courseId;
    private final TGroup tGroup;
    private final Index weekNumber;

    /**
     * Creates an {@code UnCancelWeekCommand} to uncancel a specific week
     * for the given course and tutorial group.
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @param weekNumber The week number (1-based index from user input).
     */
    public UnCancelWeekCommand(CourseId courseId, TGroup tGroup, Index weekNumber) {
        requireAllNonNull(courseId, tGroup, weekNumber);
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.weekNumber = weekNumber;
    }

    /**
     * Executes the uncancel week command.
     *
     * <p>Validates that:
     * <ul>
     *     <li>The course and tutorial group exist</li>
     *     <li>The week number is within valid bounds</li>
     *     <li>The week is currently marked as cancelled</li>
     * </ul>
     *
     * <p>If validation passes, the specified week will be removed from the
     * cancelled weeks list in the model.
     *
     * @param model The model which the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     * @throws CommandException If any validation fails.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        int weekIdx = weekNumber.getZeroBased();
        parsedInputValidation(model, weekIdx);
        model.removeCancelledWeek(courseId, tGroup, weekNumber.getZeroBased());

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                weekNumber.getOneBased(),
                courseId,
                tGroup));
    }

    private void parsedInputValidation(Model model, int weekIdx) throws CommandException {
        if (!model.hasCourseTGroup(courseId, tGroup)) {
            throw new CommandException(
                    String.format(MESSAGE_COURSE_TUT_INVALID, courseId, tGroup));
        }
        if (!isValidWeek(weekIdx)) {
            throw new CommandException(WeekList.MESSAGE_INVALID_WEEK);
        }
        if (!model.isWeekCancelled(courseId, tGroup, weekIdx)) {
            throw new CommandException(String.format(
                    MESSAGE_NOT_CANCELLED,
                    weekNumber.getOneBased(),
                    courseId,
                    tGroup));
        }
    }
    /**
     * Checks if week index is within valid bounds.
     */
    private boolean isValidWeek(int weekIdx) {
        return weekIdx >= 0 && weekIdx < WeekList.NUMBER_OF_WEEKS;
    }
    /**
     * Compares this command with another object for equality.
     *
     * @param other The object to compare with.
     * @return {@code true} if {@code other} is an {@code UnCancelWeekCommand}
     *         with the same courseId, tutorial group, and week number.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnCancelWeekCommand)) {
            return false;
        }
        UnCancelWeekCommand otherCommand = (UnCancelWeekCommand) other;
        return courseId.equals(otherCommand.courseId)
                && tGroup.equals(otherCommand.tGroup)
                && weekNumber.equals(otherCommand.weekNumber);
    }
}
