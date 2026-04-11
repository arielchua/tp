package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CancelWeekCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new CancelCommand object.
 */
public class CancelWeekCommandParser implements Parser<CancelWeekCommand> {

    private static final Prefix[] PREFIXES = {PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_WEEK};
    @Override
    public CancelWeekCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIXES);

        validateInput(args, argMultimap);

        CourseId courseId = parseCourseId(argMultimap);
        TGroup tGroup = parseTGroup(argMultimap);
        Index week = parseWeek(argMultimap);

        return new CancelWeekCommand(courseId, tGroup, week);
    }

    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        ParserValidators.ensureAllPrefixesPresent(
                argMultimap,
                PREFIXES,
                new String[]{"crs/", "tg/", "wk/"},
                CancelWeekCommand.MESSAGE_USAGE
        );

        ParserValidators.checkForUnknownPrefixTokens(args, PREFIXES,
                "crs/, tg/, and wk/",
                CancelWeekCommand.MESSAGE_USAGE);

        ParserValidators.checkForUnexpectedPreamble(argMultimap,
                CancelWeekCommand.MESSAGE_USAGE);

        ParserValidators.checkForMissingValues(
                argMultimap,
                PREFIXES,
                new String[]{"crs/", "tg/", "wk/"},
                new String[]{
                    "Course ID cannot be empty.",
                    "Tutorial group cannot be empty.",
                    "Week number cannot be empty."
                },
                CancelWeekCommand.MESSAGE_USAGE
        );

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIXES);
    }

    private CourseId parseCourseId(ArgumentMultimap argMultimap) throws ParseException {
        return ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get());
    }

    private TGroup parseTGroup(ArgumentMultimap argMultimap) throws ParseException {
        return ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get());
    }

    private Index parseWeek(ArgumentMultimap argMultimap) throws ParseException {
        return ParserUtil.parseWeekIndex(argMultimap.getValue(PREFIX_WEEK).get());
    }
}
