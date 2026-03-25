package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ABSENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final String MESSAGE_INVALID_PREFIX =
            "Invalid prefix in filter command. Allowed prefixes are: crs/, tg/, p/, and abs/.\n"
                    + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_UNEXPECTED_PREAMBLE =
            "Unexpected text before prefixes.\n" + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_EMPTY_COURSE_ID =
            "Missing value for prefix: crs/\nCourse ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_EMPTY_TGROUP =
            "Missing value for prefix: tg/\nTutorial group cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_EMPTY_PROGRESS =
            "Missing value for prefix: p/\nProgress cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_EMPTY_ABSENCE =
            "Missing value for prefix: abs/\nAbsence count cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    private static final String MESSAGE_NO_FILTERS =
            "At least one filter must be provided.\n" + FilterCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE);

        validateInput(args, argMultimap);

        Optional<CourseId> courseId = parseCourseId(argMultimap);
        Optional<TGroup> tGroup = parseTGroup(argMultimap);
        Optional<Progress> progress = parseProgress(argMultimap);
        Optional<Integer> absenceCount = parseAbsenceCount(argMultimap);

        ensureAtLeastOneFilter(courseId, tGroup, progress, absenceCount);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount));
    }

    /**
     * Validates the overall structure of the filter command input.
     */
    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        checkForInvalidPrefixes(args, argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE);
        checkForMissingValues(argMultimap);
    }

    /**
     * Checks for invalid prefixes and unexpected text before valid prefixes.
     */
    private void checkForInvalidPrefixes(String args, ArgumentMultimap argMultimap) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        for (String part : trimmedArgs.split("\\s+")) {
            if (part.contains("/")
                    && !part.startsWith(PREFIX_COURSEID.getPrefix())
                    && !part.startsWith(PREFIX_TGROUP.getPrefix())
                    && !part.startsWith(PREFIX_PROGRESS.getPrefix())
                    && !part.startsWith(PREFIX_ABSENCE.getPrefix())) {
                throw new ParseException(MESSAGE_INVALID_PREFIX);
            }
        }

        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_PREAMBLE);
        }
    }

    /**
     * Checks for missing values after prefixes.
     */
    private void checkForMissingValues(ArgumentMultimap argMultimap) throws ParseException {
        if (hasBlankValue(argMultimap, PREFIX_COURSEID)) {
            throw new ParseException(MESSAGE_EMPTY_COURSE_ID);
        }

        if (hasBlankValue(argMultimap, PREFIX_TGROUP)) {
            throw new ParseException(MESSAGE_EMPTY_TGROUP);
        }

        if (hasBlankValue(argMultimap, PREFIX_PROGRESS)) {
            throw new ParseException(MESSAGE_EMPTY_PROGRESS);
        }

        if (hasBlankValue(argMultimap, PREFIX_ABSENCE)) {
            throw new ParseException(MESSAGE_EMPTY_ABSENCE);
        }
    }

    /**
     * Ensures that at least one filter field is present.
     */
    private void ensureAtLeastOneFilter(Optional<CourseId> courseId, Optional<TGroup> tGroup,
                                        Optional<Progress> progress, Optional<Integer> absenceCount)
            throws ParseException {
        if (courseId.isEmpty() && tGroup.isEmpty() && progress.isEmpty() && absenceCount.isEmpty()) {
            throw new ParseException(MESSAGE_NO_FILTERS);
        }
    }

    /**
     * Returns the parsed course ID if present.
     */
    private Optional<CourseId> parseCourseId(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = argMultimap.getValue(PREFIX_COURSEID);
        return value.isPresent()
                ? Optional.of(ParserUtil.parseCourseId(value.get()))
                : Optional.empty();
    }

    /**
     * Returns the parsed tutorial group if present.
     */
    private Optional<TGroup> parseTGroup(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = argMultimap.getValue(PREFIX_TGROUP);
        return value.isPresent()
                ? Optional.of(ParserUtil.parseTGroup(value.get()))
                : Optional.empty();
    }

    /**
     * Returns the parsed progress value if present.
     */
    private Optional<Progress> parseProgress(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = argMultimap.getValue(PREFIX_PROGRESS);
        return value.isPresent()
                ? Optional.of(ParserUtil.parseProgress(value.get()))
                : Optional.empty();
    }

    /**
     * Returns the parsed absence count if present.
     */
    private Optional<Integer> parseAbsenceCount(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = argMultimap.getValue(PREFIX_ABSENCE);
        return value.isPresent()
                ? Optional.of(ParserUtil.parseAbsenceCount(value.get()))
                : Optional.empty();
    }

    /**
     * Returns true if the given prefix is present but its value is blank.
     */
    private boolean hasBlankValue(ArgumentMultimap argMultimap, Prefix prefix) {
        return argMultimap.getValue(prefix).isPresent()
                && argMultimap.getValue(prefix).get().trim().isEmpty();
    }
}
