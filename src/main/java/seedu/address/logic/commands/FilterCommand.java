package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Person;

/**
 * Filters the student list by course ID, tutorial group, progress, and/or absence count.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters students by course, tutorial group, progress, and/or absence count.\n"
            + "Parameters: [crs/COURSE_ID] [tg/TUTORIAL_GROUP] [p/PROGRESS] [abs/ABSENCE_COUNT]\n"
            + "At least one parameter must be provided.\n"
            + "Example: " + COMMAND_WORD + " crs/CS2103T tg/T01 p/on_track abs/2";

    public static final String MESSAGE_SUCCESS = "There are %d students matching this filter.";

    private final FilterMatchesPredicate predicate;

    /**
     * Creates a {@code FilterCommand} using the given predicate.
     *
     * @param predicate Predicate used to filter students by course ID,
     *                  tutorial group, progress, and/or absence count.
     */
    public FilterCommand(FilterMatchesPredicate predicate) {
        requireNonNull(predicate);
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
