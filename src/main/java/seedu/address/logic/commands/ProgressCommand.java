package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;

/**
 * Updates the progress status of a student.
 */
public class ProgressCommand extends Command {

    public static final String COMMAND_WORD = "progress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the progress of a student.\n"
            + "Progress values:\n"
            + "  on_track - student is doing well\n"
            + "  needs_attention - student may need closer monitoring\n"
            + "  at_risk - student requires urgent attention\n"
            + "  clear - removes the progress status\n"
            + "Format 1: " + COMMAND_WORD + " INDEX p/PROGRESS\n"
            + "Example: " + COMMAND_WORD + " 1 p/on_track\n"
            + "Format 2: " + COMMAND_WORD + " id/STUDENT_ID crs/COURSE_ID tg/TGROUP p/PROGRESS\n"
            + "Example: " + COMMAND_WORD + " id/A0301200M crs/CS2103 tg/T01 p/on_track";

    public static final String MESSAGE_UPDATE_PROGRESS_SUCCESS = "Updated progress for student: %1$s to %2$s";
    public static final String MESSAGE_CLEAR_PROGRESS_SUCCESS = "Cleared progress for student: %1$s";
    public static final String MESSAGE_STUDENT_NOT_FOUND =
            "No student found with the given student ID, course ID, and tutorial group.";

    private final Index index;
    private final StudentId studentId;
    private final CourseId courseId;
    private final TGroup tGroup;
    private final Progress progress;
    private final boolean isIndexMode;

    /**
     * Creates a ProgressCommand using index mode.
     */
    public ProgressCommand(Index index, Progress progress) {
        requireNonNull(index);
        requireNonNull(progress);

        this.index = index;
        this.progress = progress;
        this.studentId = null;
        this.courseId = null;
        this.tGroup = null;
        this.isIndexMode = true;
    }

    /**
     * Creates a ProgressCommand using student identity mode.
     */
    public ProgressCommand(StudentId studentId, CourseId courseId, TGroup tGroup, Progress progress) {
        requireNonNull(studentId);
        requireNonNull(courseId);
        requireNonNull(tGroup);
        requireNonNull(progress);

        this.index = null;
        this.studentId = studentId;
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.progress = progress;
        this.isIndexMode = false;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToEdit;

        if (isIndexMode) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToEdit = lastShownList.get(index.getZeroBased());
        } else {
            personToEdit = findPersonByIdentity(lastShownList);
            if (personToEdit == null) {
                throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
            }
        }

        Person editedPerson = createEditedPerson(personToEdit, progress);

        model.setPerson(personToEdit, editedPerson); 

        if (progress == Progress.NOT_SET) {
            return new CommandResult(String.format(
                    MESSAGE_CLEAR_PROGRESS_SUCCESS, editedPerson.getName()));
        }

        return new CommandResult(String.format(
                MESSAGE_UPDATE_PROGRESS_SUCCESS, editedPerson.getName(), editedPerson.getProgress()));
    }

    /**
     * Finds a person by student identity: student ID + course ID + tutorial group.
     */
    private Person findPersonByIdentity(List<Person> personList) {
        for (Person person : personList) {
            boolean sameStudentId = person.getStudentId().equals(studentId);
            boolean sameCourseId = person.getCourseId().equals(courseId);
            boolean sameTGroup = person.getTGroup().equals(tGroup);

            if (sameStudentId && sameCourseId && sameTGroup) {
                return person;
            }
        }
        return null;
    }

    /**
     * Returns a person with the same fields as {@code personToEdit}, but with updated progress.
     */
    private static Person createEditedPerson(Person personToEdit, Progress updatedProgress) {
        assert personToEdit != null;

        return new Person(
                personToEdit.getName(),
                personToEdit.getCourseId(),
                personToEdit.getEmail(),
                personToEdit.getStudentId(),
                personToEdit.getTGroup(),
                personToEdit.getTele(),
                updatedProgress);
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

        if (isIndexMode != otherCommand.isIndexMode) {
            return false;
        }

        if (!progress.equals(otherCommand.progress)) {
            return false;
        }

        if (isIndexMode) {
            return index.equals(otherCommand.index);
        }

        return studentId.equals(otherCommand.studentId)
                && courseId.equals(otherCommand.courseId)
                && tGroup.equals(otherCommand.tGroup);
    }
}