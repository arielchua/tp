package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ABSENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Arrays;
import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new FilterCommand object.
 * Logic involves validating the presence of prefixes, ensuring no illegal preambles,
 * and parsing specific values into an appropriate predicate.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    // Error Messages (made public for use in tests) — delegate wording to ParserMessages to ensure
    // consistent parser UX across commands while keeping these constants public for tests.
    public static final String MESSAGE_INVALID_PREFIX = ParserMessages.invalidPrefix(
            "crs/, tg/, p/, and abs./", FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_UNEXPECTED_PREAMBLE = ParserMessages.unexpectedPreamble(
            FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_EMPTY_COURSE_ID = ParserMessages.missingPrefixValue(
            "crs/", "Course ID cannot be empty.", FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_EMPTY_TGROUP = ParserMessages.missingPrefixValue(
            "tg/", "Tutorial group cannot be empty.", FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_EMPTY_PROGRESS = ParserMessages.missingPrefixValue(
            "p/", "Progress cannot be empty.", FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_EMPTY_ABSENCE = ParserMessages.missingPrefixValue(
            "abs/", "Absence count cannot be empty.", FilterCommand.MESSAGE_USAGE);
    public static final String MESSAGE_NO_FILTERS =
            "At least one filter must be provided.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH = ParserMessages.possiblePrefixMissingSlash(
            FilterCommand.MESSAGE_USAGE);
    /**
     * The list of prefixes that this parser is able to handle.
     */
    private static final Prefix[] FILTER_PREFIXES = {
        PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE
    };


    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @param args The raw user input.
     * @return A FilterCommand object.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, FILTER_PREFIXES);

        validateInput(args, argMultimap);

        Optional<CourseId> courseId = parseCourseId(argMultimap);
        Optional<TGroup> tGroup = parseTGroup(argMultimap);
        Optional<Progress> progress = parseProgress(argMultimap);
        Optional<Integer> absenceCount = parseAbsenceCount(argMultimap);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount));
    }

    /**
     * Orchestrates various structural and logical validation checks on the tokenized input.
     * @param args        The raw input string.
     * @param argMultimap The map of tokens to values.
     * @throws ParseException if validation fails.
     */
    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        ParserValidators.checkForBarePrefixes(argMultimap, FILTER_PREFIXES, FilterCommand.MESSAGE_USAGE);
        ParserValidators.checkForUnknownPrefixTokens(args, FILTER_PREFIXES, "crs/, tg/, p/, and abs/",
                FilterCommand.MESSAGE_USAGE);
        ParserValidators.checkForUnexpectedPreamble(argMultimap, FilterCommand.MESSAGE_USAGE);
        ensureAtLeastOneFilterPresent(argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);
        ParserValidators.checkForMissingValues(argMultimap, FILTER_PREFIXES,
                new String[] {"crs/", "tg/", "p/", "abs/"},
                new String[] {"Course ID cannot be empty.", "Tutorial group cannot be empty.",
                    "Progress cannot be empty.", "Absence count cannot be empty."},
                FilterCommand.MESSAGE_USAGE);
    }

    /**
     * Ensures that the user has provided at least one criteria to filter by.
     */
    private void ensureAtLeastOneFilterPresent(ArgumentMultimap argMultimap) throws ParseException {
        boolean anyPresent = Arrays.stream(FILTER_PREFIXES)
                .anyMatch(prefix -> argMultimap.getValue(prefix).isPresent());
        if (!anyPresent) {
            throw new ParseException(MESSAGE_NO_FILTERS);
        }
    }

    /**
     * Parses the CourseId field.
     */
    private Optional<CourseId> parseCourseId(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_COURSEID).isPresent()
                ? Optional.of(ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get()))
                : Optional.empty();
    }

    /**
     * Parses the Tutorial Group field.
     */
    private Optional<TGroup> parseTGroup(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_TGROUP).isPresent()
                ? Optional.of(ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get()))
                : Optional.empty();
    }

    /**
     * Parses the Progress status field.
     */
    private Optional<Progress> parseProgress(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_PROGRESS).isPresent()
                ? Optional.of(ParserUtil.parseProgress(argMultimap.getValue(PREFIX_PROGRESS).get()))
                : Optional.empty();
    }

    /**
     * Parses the Absence count field.
     */
    private Optional<Integer> parseAbsenceCount(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_ABSENCE).isPresent()
                ? Optional.of(ParserUtil.parseAbsenceCount(argMultimap.getValue(PREFIX_ABSENCE).get()))
                : Optional.empty();
    }
}
