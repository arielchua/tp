package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Procedural validation helpers for parsers. These methods perform checks on the
 * tokenized arguments and throw {@link ParseException} with consistent messages
 * produced by {@link ParserMessages}.
 */
public final class ParserValidators {

    private ParserValidators() {
    }

    /**
     * Scans the raw input string for tokens that contain a slash and verifies
     * that they start with one of the {@code allowedPrefixes}.
     */
    public static void checkForUnknownPrefixTokens(String args, Prefix[] allowedPrefixes,
                                                   String allowedPrefixesHumanReadable,
                                                   String commandUsage) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        for (String part : trimmedArgs.split("\\s+")) {
            if (!part.contains("/")) {
                continue;
            }
            boolean matchesAny = Arrays.stream(allowedPrefixes)
                    .anyMatch(prefix -> part.startsWith(prefix.getPrefix()));
            if (!matchesAny) {
                throw new ParseException(ParserMessages.invalidPrefix(allowedPrefixesHumanReadable, commandUsage));
            }
        }
    }

    /**
     * Ensures that the preamble is empty.
     */
    public static void checkForUnexpectedPreamble(ArgumentMultimap argMultimap, String commandUsage)
            throws ParseException {
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(ParserMessages.unexpectedPreamble(commandUsage));
        }
    }

    /**
     * Checks that the given prefixes have non-blank values.
     */
    public static void checkForMissingValues(ArgumentMultimap argMultimap, Prefix[] prefixes,
                                             String[] prefixStrings, String[] detailMessages,
                                             String commandUsage) throws ParseException {
        assert prefixes.length == prefixStrings.length : "Prefix arrays mismatch";
        assert prefixes.length == detailMessages.length : "Detail message arrays mismatch";

        for (int i = 0; i < prefixes.length; i++) {
            if (argMultimap.isValueBlank(prefixes[i])) {
                throw new ParseException(
                        ParserMessages.missingPrefixValue(prefixStrings[i], detailMessages[i], commandUsage));
            }
        }
    }

    /**
     * Checks that all prefixes are present.
     *
     * @param argMultimap   the token map
     * @param prefixes      array of Prefix
     * @param prefixStrings human-readable prefix strings (e.g., "crs/")
     * @param commandUsage  command usage to include in the error message
     * @throws ParseException when any prefix is not present
     */
    public static void ensureAllPrefixesPresent(
            ArgumentMultimap argMultimap,
            Prefix[] prefixes,
            String[] prefixStrings,
            String commandUsage) throws ParseException {

        List<String> missing = getMissingList(argMultimap, prefixes, prefixStrings);

        if (!missing.isEmpty()) {
            throw new ParseException(
                    "Missing required prefix(es): " + String.join(", ", missing)
                            + "\n" + commandUsage);
        }
    }

    /**
     * Checks that all prefixes and the Index are present.
     *
     * @param argMultimap   the token map
     * @param prefixes      array of Prefix
     * @param prefixStrings human-readable prefix strings (e.g., "crs/")
     * @param commandUsage  command usage to include in the error message
     * @throws ParseException when any prefix is not present
     */
    public static void ensureIndexAndPrefixesPresent(
            ArgumentMultimap argMultimap,
            Prefix[] prefixes,
            String[] prefixStrings,
            String commandUsage) throws ParseException {

        List<String> missingParts = new ArrayList<>();
        if (argMultimap.getPreamble().trim().isEmpty()) {
            missingParts.add("student index");
        }

        List<String> missingPrefixes = getMissingList(argMultimap, prefixes, prefixStrings);
        if (!missingPrefixes.isEmpty()) {
            missingParts.add("prefix(es): " + String.join(", ", missingPrefixes));
        }

        if (!missingParts.isEmpty()) {
            throw new ParseException(
                    "Missing required: " + String.join(" AND ", missingParts)
                            + "\n" + commandUsage
            );
        }
    }

    /**
     * Internal helper to identify missing prefixes.
     */
    private static List<String> getMissingList(ArgumentMultimap argMultimap,
                                               Prefix[] prefixes, String[] prefixStrings) {
        List<String> missing = new ArrayList<>();
        for (int i = 0; i < prefixes.length; i++) {
            if (argMultimap.getValue(prefixes[i]).isEmpty()) {
                missing.add(prefixStrings[i]);
            }
        }
        return missing;
    }
}

