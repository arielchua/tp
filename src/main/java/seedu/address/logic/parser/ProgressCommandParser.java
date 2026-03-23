package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new ProgressCommand object.
 */
public class ProgressCommandParser implements Parser<ProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ProgressCommand
     * and returns a ProgressCommand object for execution.
     * @throws ParseException if the user input does not follow the expected format
     */
    public ProgressCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS);

        boolean hasStudentId = argMultimap.getValue(PREFIX_STUDENTID).isPresent();
        boolean hasCourseId = argMultimap.getValue(PREFIX_COURSEID).isPresent();
        boolean hasTGroup = argMultimap.getValue(PREFIX_TGROUP).isPresent();
        boolean hasProgress = argMultimap.getValue(PREFIX_PROGRESS).isPresent();

        // assume trying identity mode as long as one student field is provided
        boolean isIdentityMode = hasStudentId || hasCourseId || hasTGroup;

        if (!hasProgress) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
        }

        // reject duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS);

        // parse progress value
        Progress progress = ParserUtil.parseProgress(argMultimap.getValue(PREFIX_PROGRESS).get());

        if (isIdentityMode) {
            // all fields must be present and not have index
            if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS)
                    || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
            }

            StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
            CourseId courseId = ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get());
            TGroup tGroup = ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get());

            return new ProgressCommand(studentId, courseId, tGroup, progress);
        } else {
            if (argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
            }

            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

            return new ProgressCommand(index, progress);
        }
    }

    /**
     * Returns true if all the specified prefixes are present in the given ArgumentMultimap.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
