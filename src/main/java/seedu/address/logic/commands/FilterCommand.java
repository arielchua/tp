package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.person.CourseIdMatchesPredicate;
import seedu.address.model.person.Person;

/**
 * Filters the student list by course ID.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters students by course.\n"
            + "Parameters: crs/COURSE\n"
            + "Example: " + COMMAND_WORD + " crs/CS2030S";

    public static final String MESSAGE_SUCCESS = "There are %d students matching this filter.";

    private final java.util.function.Predicate<Person> predicate;

    /**
     * Creates a {@code FilterCommand} using the given predicate.
     *
     * @param predicate Predicate used to filter persons by course ID.
     */
    public FilterCommand(CourseIdMatchesPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Filters the person list in the model using the stored predicate and returns
     * the number of matching students.
     *
     * @param model {@code Model} which the command should operate on.
     * @return A {@code CommandResult} showing the number of matching students.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> filteredList = model.getFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_SUCCESS, filteredList.size()));
    }

    /**
     * Returns true if the other object is equal to this {@code FilterCommand}.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FilterCommand
                && predicate.equals(((FilterCommand) other).predicate));
    }
}
