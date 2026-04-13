package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
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
 * Logic involves validating the presence of prefixes, ensuring no illegal preambles,
 * and parsing specific values into an appropriate predicate.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Functional interface for a parser function that can throw ParseException.
     * @param <T> The type of the input string.
     * @param <R> The type of the result.
     */
    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws ParseException;
    }

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

        Optional<CourseId> courseId = parseField(argMultimap, PREFIX_COURSEID, ParserUtil::parseCourseId);
        Optional<TGroup> tGroup = parseField(argMultimap, PREFIX_TGROUP, ParserUtil::parseTGroup);
        Optional<Progress> progress = parseField(argMultimap, PREFIX_PROGRESS, ParserUtil::parseProgress);
        Optional<Integer> absenceCount = parseField(argMultimap, PREFIX_ABSENCE, ParserUtil::parseAbsenceCount);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount));
    }

    /**
     * A generic helper method to parse a value from the argument multimap.
     * Note: This method was generated with the assistance of GitHub Copilot to
     * handle generic functional parsing of CLI arguments.
     * @param argMultimap The map of arguments.
     * @param prefix The prefix to look for.
     * @param parser The function to parse the string value.
     * @return An optional containing the parsed value, or empty if the prefix is not present.
     * @throws ParseException if the value is present but invalid.
     */
    private <T> Optional<T> parseField(ArgumentMultimap argMultimap, Prefix prefix,
                                       ThrowingFunction<String, T> parser) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(parser.apply(value.get()));
    }

    /**
     * Orchestrates various structural and logical validation checks on the tokenized input.
     * @param args        The raw input string.
     * @param argMultimap The map of tokens to values.
     * @throws ParseException if validation fails.
     */
    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        ParserValidators.checkForUnknownPrefixTokens(args, FILTER_PREFIXES, "crs/, tg/, p/, and abs/",
                FilterCommand.MESSAGE_USAGE);
        ParserValidators.ensureAtLeastOnePrefixPresent(argMultimap, FILTER_PREFIXES, FilterCommand.MESSAGE_USAGE);
        ParserValidators.checkForUnexpectedPreamble(argMultimap, FilterCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);
        ParserValidators.checkForMissingValues(argMultimap, FILTER_PREFIXES,
                new String[] {"crs/", "tg/", "p/", "abs/"},
                new String[] {"Course ID cannot be empty.", "Tutorial group cannot be empty.",
                    "Progress cannot be empty.", "Absence count cannot be empty."},
                FilterCommand.MESSAGE_USAGE);
    }

}
