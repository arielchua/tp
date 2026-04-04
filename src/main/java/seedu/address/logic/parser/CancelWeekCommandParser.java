package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CancelWeekCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new UnCancelCommand object.
 */
public class CancelWeekCommandParser implements Parser<CancelWeekCommand> {

    @Override
    public CancelWeekCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_WEEK);

        // Ensure required prefixes exist
        if (!argMultimap.getValue(PREFIX_COURSEID).isPresent()
                || !argMultimap.getValue(PREFIX_TGROUP).isPresent()
                || !argMultimap.getValue(PREFIX_WEEK).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            CancelWeekCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_WEEK);
        CourseId courseId = ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get());
        TGroup tGroup = ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get());
        Index week = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_WEEK).get());

        return new CancelWeekCommand(courseId, tGroup, week);
    }
}
