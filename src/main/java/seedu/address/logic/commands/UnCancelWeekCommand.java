package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.WeekList;


/**
 * Marks the specified week (tutorial) as UnCancelled for the same (CourseId-Tutorial) pair
 */
public class UnCancelWeekCommand extends Command {

    public static final String COMMAND_WORD = "uncancelweek";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Uncancels the week from attendance table.\n"
            + "Parameters: [crs/COURSE_ID] [tg/TUTORIAL_ID]"
            + PREFIX_WEEK + "WEEK_NUMBER\n "
            + "All parameters must be included\n"
            + "Example: " + COMMAND_WORD + " crs/CS2103T tg/T01 week/5";

    public static final String MESSAGE_SUCCESS =
            "Week %1$d uncancelled for course %2$s tutorial %3$s";

    private final CourseId courseId;
    private final TGroup tGroup;
    private final Index weekNumber;

    /**
     * Creates an UnCancelCommand to Uncancel a
     * specific week for a courseid-tutorial pair.
     * @param courseId
     * @param tGroup
     * @param weekNumber week of tutorial to cancel
     */
    public UnCancelWeekCommand(CourseId courseId, TGroup tGroup, Index weekNumber) {
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.weekNumber = weekNumber;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        model.removeCancelledWeek(courseId, tGroup, weekNumber.getZeroBased());

        // update existing persons
        for (Person person : model.getFilteredPersonList()) {
            if (person.getCourseId().equals(courseId)
                    && person.getTGroup().equals(tGroup)) {

                WeekList weekList = ((WeekList) person.getWeeklyAttendanceList()).copy();

                weekList.markWeekAsDefault(weekNumber.getZeroBased());

                Person updated = new Person(
                        person.getName(),
                        person.getCourseId(),
                        person.getEmail(),
                        person.getStudentId(),
                        person.getTGroup(),
                        person.getTele(),
                        weekList,
                        person.getProgress()
                );

                model.setPerson(person, updated);
            }
        }

        return new CommandResult("Week uncancelled successfully");
    }
}
