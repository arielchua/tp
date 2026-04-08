package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
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
    private static final Prefix[] PREFIXES = {PREFIX_WEEK, PREFIX_STATUS};

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIXES);

        validateInput(args, argMultimap);

        Index index = ParserUtil.parseIndex(
                argMultimap.getPreamble(),
                MarkAttendanceCommand.MESSAGE_USAGE
        );

        Index week = parseWeek(argMultimap);
        Week.Status status = parseStatus(argMultimap);

        return new MarkAttendanceCommand(index, week, status);
    }

    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        ParserValidators.ensureIndexAndPrefixesPresent(
                argMultimap,
                PREFIXES,
                new String[]{"week/", "sta/"},
                MarkAttendanceCommand.MESSAGE_USAGE
        );

        ParserValidators.checkForBarePrefixes(argMultimap, PREFIXES,
                MarkAttendanceCommand.MESSAGE_USAGE);

        ParserValidators.checkForUnknownPrefixTokens(args, PREFIXES,
                "week/ and sta/",
                MarkAttendanceCommand.MESSAGE_USAGE);

        ParserValidators.checkForMissingValues(
                argMultimap,
                PREFIXES,
                new String[]{"week/", "sta/"},
                new String[]{
                    "Week number cannot be empty.",
                    "Status cannot be empty."
                },
                MarkAttendanceCommand.MESSAGE_USAGE
        );

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIXES);
    }

    private Index parseWeek(ArgumentMultimap argMultimap) throws ParseException {
        return ParserUtil.parseWeekIndex(argMultimap.getValue(PREFIX_WEEK).get());
    }

    private Week.Status parseStatus(ArgumentMultimap argMultimap) throws ParseException {
        return ParserUtil.parseWeekStatus(argMultimap.getValue(PREFIX_STATUS).get());
    }
}
