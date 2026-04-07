package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Week;

/**
 * Parses input arguments and creates a new MarkAttendanceCommand object.
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_WEEK, PREFIX_STATUS);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            MarkAttendanceCommand.MESSAGE_USAGE), pe);
        }

        // Ensure required prefixes exist
        if (!argMultimap.getValue(PREFIX_WEEK).isPresent()
                || !argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            MarkAttendanceCommand.MESSAGE_USAGE));
        }

        /*
         Only check is status command is duplicated
         index's and weeks to be implemented later
        */
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS);

        Index weekNumber = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WEEK).get());
        Week.Status status = ParserUtil.parseAttendanceStatus(
                argMultimap.getValue(PREFIX_STATUS).get());

        return new MarkAttendanceCommand(index, weekNumber, status);
    }
}
