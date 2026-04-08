package seedu.address.logic.parser;

import java.util.Arrays;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Procedural validation helpers for parsers. These methods perform checks on the
 * tokenized arguments and throw {@link ParseException} with consistent messages
 * produced by {@link ParserMessages}.
 */
public final class ParserValidators {

    private ParserValidators() {}

    /**
     * Checks the preamble for tokens that look like prefixes but are missing the
     * trailing slash (e.g. user typed "crs" instead of "crs/"). If such a
     * token is found, a {@link ParseException} is thrown with a helpful hint
     * that includes the provided {@code commandUsage}.
     *
     * @param argMultimap the tokenized argument map to inspect
     * @param allowedPrefixes the set of prefixes that are valid for the command
     * @param commandUsage usage text of the command; appended to the error message
     * @throws ParseException if a bare prefix token is detected in the preamble
     */
    public static void checkForBarePrefixes(ArgumentMultimap argMultimap, Prefix[] allowedPrefixes,
                                           String commandUsage) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            return;
        }

        for (String token : preamble.split("\\s+")) {
            for (Prefix p : allowedPrefixes) {
                String bare = p.getPrefix().replace("/", "");
                if (token.equalsIgnoreCase(bare)) {
                    throw new ParseException(ParserMessages.possiblePrefixMissingSlash(commandUsage));
                }
            }
        }
    }

    /**
     * Scans the raw input string for tokens that contain a slash and verifies
     * that they start with one of the {@code allowedPrefixes}. If a token with
     * a slash does not match any allowed prefix, a {@link ParseException} is
     * thrown with a message describing the allowed prefixes and the command
     * usage.
     *
     * @param args the raw argument string supplied by the user
     * @param allowedPrefixes the set of prefixes that are valid for the command
     * @param allowedPrefixesHumanReadable a human-readable list of allowed prefixes
     * @param commandUsage usage text of the command; appended to the error message
     * @throws ParseException if an unknown prefix token is found
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
     * Ensures that the preamble is empty. Parsers that do not expect any text
     * before the first prefix should call this method. If non-empty preamble is
     * found, a {@link ParseException} containing the {@code commandUsage} will
     * be thrown.
     *
     * @param argMultimap the tokenized argument map to inspect
     * @param commandUsage usage text of the command; appended to the error message
     * @throws ParseException if the preamble is non-empty
     */
    public static void checkForUnexpectedPreamble(ArgumentMultimap argMultimap, String commandUsage)
            throws ParseException {
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(ParserMessages.unexpectedPreamble(commandUsage));
        }
    }

    /**
     * Checks that the given prefixes have non-blank values. The arrays must be the same length.
     * @param argMultimap the token map
     * @param prefixes array of Prefix
     * @param prefixStrings human-readable prefix strings (e.g., "crs/")
     * @param detailMessages short detail messages for each prefix (e.g., "Course ID cannot be empty.")
     * @param commandUsage command usage to include in the error message
     * @throws ParseException when a prefix is present but its value is blank
     */
    public static void checkForMissingValues(ArgumentMultimap argMultimap, Prefix[] prefixes,
                                             String[] prefixStrings, String[] detailMessages,
                                             String commandUsage) throws ParseException {
        if (prefixes.length != prefixStrings.length || prefixes.length != detailMessages.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }

        for (int i = 0; i < prefixes.length; i++) {
            if (argMultimap.isValueBlank(prefixes[i])) {
                throw new ParseException(
                        ParserMessages.missingPrefixValue(prefixStrings[i], detailMessages[i], commandUsage));
            }
        }
    }

    /**
     * Checks that all prefixes are present.
     * @param argMultimap the token map
     * @param prefixes array of Prefix
     * @param prefixStrings human-readable prefix strings (e.g., "crs/")
     * @param commandUsage command usage to include in the error message
     * @throws ParseException when any prefix is not present
     */
    public static void ensureAllPrefixesPresent(
            ArgumentMultimap argMultimap,
            Prefix[] prefixes,
            String[] prefixStrings,
            String commandUsage) throws ParseException {

        StringBuilder missing = new StringBuilder();

        for (int i = 0; i < prefixes.length; i++) {
            if (argMultimap.getValue(prefixes[i]).isEmpty()) {
                if (!missing.isEmpty()) {
                    missing.append(", ");
                }
                missing.append(prefixStrings[i]);
            }
        }

        if (!missing.isEmpty()) {
            throw new ParseException(
                    "Missing required prefix(es): " + missing.toString()
                            + "\n" + commandUsage);
        }
    }
}

