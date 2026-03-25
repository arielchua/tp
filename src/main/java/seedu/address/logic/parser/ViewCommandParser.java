package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewCommand object.
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    public static final String MESSAGE_INDEX_MISSING = "The index to view is missing.";
    public static final String MESSAGE_NOT_A_NUMBER = "The index provided must be a number.";
    public static final String MESSAGE_TOO_MANY_ARGS = "The view command only accepts one index. "
            + "Please remove extra arguments.";

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     */
    public ViewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        // Check 1: Empty input (Missing Index)
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_INDEX_MISSING + "\n" + ViewCommand.MESSAGE_USAGE);
        }

        // Check 2: Multiple arguments
        if (trimmedArgs.split("\\s+").length > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_ARGS + "\n" + ViewCommand.MESSAGE_USAGE);
        }

        // Check 3: Numeric format
        if (!trimmedArgs.matches("-?\\d+")) {
            throw new ParseException(MESSAGE_NOT_A_NUMBER + "\n" + ViewCommand.MESSAGE_USAGE);
        }

        try {
            Index index = ParserUtil.parseIndex(trimmedArgs);
            return new ViewCommand(index);
        } catch (ParseException pe) {
            // This catches "0" or negative numbers via ParserUtil
            throw new ParseException(pe.getMessage() + "\n" + ViewCommand.MESSAGE_USAGE);
        }
    }
}
