package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.WeekList;
import seedu.address.model.person.WeeklyAttendanceList;

/**
 * Marks the specified week (tutorial) as Cancelled for the same (CourseId-Tutorial) pair
 */
public class CancelWeekCommand extends Command {

    public static final String COMMAND_WORD = "cancelweek";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels the week from attendance table.\n"
            + "Parameters: [crs/COURSE_ID] [tg/TUTORIAL_ID]"
            + PREFIX_WEEK + "WEEK_NUMBER\n "
            + "All parameters must be included\n"
            + "Example: " + COMMAND_WORD + " crs/CS2103T tg/T01 week/5";

    public static final String MESSAGE_SUCCESS =
            "Week %1$d cancelled for course %2$s tutorial %3$s";

    public final CourseId courseId;
    public final TGroup tGroup;
    private final Index weekNumber;

    /**
     * Creates an CancelWeekCommand to cancel the week in attendance list
     * of all students with the same courseID and tGroup
     *
     * @param courseId ID of a particular course
     * @param tGroup tutorial group of particular course
     */
    public CancelWeekCommand(CourseId courseId, TGroup tGroup, Index weekNumber) {
        requireAllNonNull(courseId, tGroup);
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.weekNumber = weekNumber;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> persons = model.getFilteredPersonList();
        if (weekNumber.getZeroBased() >= WeekList.NUMBER_OF_WEEKS) {
            throw new CommandException("Invalid Week, there are only 13 weeks");
        }
        for (Person personToEdit : persons) {
            if (personToEdit.getCourseId().equals(courseId)
                    && personToEdit.getTGroup().equals(tGroup)) {

                WeeklyAttendanceList weekList = ((WeekList) personToEdit.getWeeklyAttendanceList()).copy();

                try {
                    weekList.markAsCancelled(weekNumber.getZeroBased());
                } catch (IllegalStateException e) {
                    // ignore duplicates
                    continue;
                }

                Person editedPerson = new Person(
                        personToEdit.getName(),
                        personToEdit.getCourseId(),
                        personToEdit.getEmail(),
                        personToEdit.getStudentId(),
                        personToEdit.getTGroup(),
                        personToEdit.getTele(),
                        weekList,
                        personToEdit.getProgress()
                );

                model.setPerson(personToEdit, editedPerson);
            }
        }
        model.addCancelledWeek(courseId, tGroup, weekNumber.getZeroBased());
        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                weekNumber.getOneBased(),
                courseId,
                tGroup));
    }
}
