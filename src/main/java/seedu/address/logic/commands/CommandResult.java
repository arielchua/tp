package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean helpRequest;

    /** The application should exit. */
    private final boolean exit;

    /** The person to show in the view window. {@code null} if no person should be shown. */
    private final Person personToView;

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default values.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, null, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, null, showHelp, exit);
    }

    /**
     * Constructs a {@code CommandResult} for showing a person's details in the view window.
     */
    public CommandResult(String feedbackToUser, Person personToView) {
        this(feedbackToUser, personToView, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, Person personToView, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.personToView = personToView;
        this.helpRequest = showHelp;
        this.exit = exit;

        assert !(showHelp && exit) : "CommandResult cannot show help and exit at the same time";
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isHelpRequest() {
        return helpRequest;
    }

    public boolean shouldShowView() {
        return personToView != null;
    }

    /**
     * Returns the person to show in the view window, or {@code null} if no person should be shown.
     */
    public Person getPersonToView() {
        return personToView;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CommandResult otherCommandResult)) {
            return false;
        }

        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && helpRequest == otherCommandResult.helpRequest
                && exit == otherCommandResult.exit
                && Objects.equals(personToView, otherCommandResult.personToView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, helpRequest, exit, personToView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("helpRequest", helpRequest)
                .add("personToView", personToView)
                .add("exit", exit)
                .toString();
    }
}
