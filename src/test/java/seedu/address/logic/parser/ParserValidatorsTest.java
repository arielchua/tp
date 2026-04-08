package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains structural validation logic for command parsers.
 * * This class provides utility methods to verify that user input adheres to the
 * expected command format, specifically regarding prefix presence, preamble
 * constraints, and value validity. These tests cover standard Equivalence
 * Partitions (EP) for command-line orchestration.
 */
public class ParserValidatorsTest {
    private static final String USAGE = FilterCommand.MESSAGE_USAGE;
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_noValidFilters_throwsParseException() {
        // EP: Input consists only of text that looks like prefixes but missing slashes
        // Logic: Since slashes are missing, they are treated as preamble, triggering the "No Filters" check.
        assertParseFailure(parser, " abs 5",
                FilterCommandParser.MESSAGE_NO_FILTERS);

        // EP: Multiple bare tokens
        assertParseFailure(parser, " abs crs tg p",
                FilterCommandParser.MESSAGE_NO_FILTERS);

        // EP: Case-insensitive bare tokens
        assertParseFailure(parser, " ABS 5",
                FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void checkForUnknownPrefixTokens_invalidPrefix_throwsParseException() {
        // EP: Unknown prefix 'v/' buried among valid ones
        assertParseFailure(parser, " crs/CS2103T v/Unknown tg/T01",
                ParserMessages.invalidPrefix("crs/, tg/, p/, and abs/", USAGE));

        // EP: Standalone slash or unknown prefix string
        assertParseFailure(parser, " /invalid",
                ParserMessages.invalidPrefix("crs/, tg/, p/, and abs/", USAGE));
    }

    @Test
    public void checkForUnexpectedPreamble_nonEmptyPreamble_throwsParseException() {
        // EP: Text provided before valid prefixes
        assertParseFailure(parser, " This is a preamble crs/CS2103T",
                ParserMessages.unexpectedPreamble(USAGE));
    }

    @Test
    public void checkForMissingValues_emptyPrefixValue_throwsFirstParseException() {
        // EP: Prefix present but value is blank
        // Logic: Should catch the first empty prefix encountered in the validation sequence (crs/)
        assertParseFailure(parser, " crs/ tg/T01",
                FilterCommandParser.MESSAGE_EMPTY_COURSE_ID);
    }

    @Test
    public void checkForMissingValues_mismatchedArrays_throwsAssertionError() {
        // EP: Developer error where parallel arrays in the utility method are out of sync
        ArgumentMultimap map = new ArgumentMultimap();

        // Note: Code uses 'assert' keyword, which throws AssertionError in testing environments
        assertThrows(AssertionError.class, () ->
                ParserValidators.checkForMissingValues(map,
                        new Prefix[]{new Prefix("a/")},
                        new String[]{}, // Empty array causes mismatch
                        new String[]{"Detail"}, USAGE));
    }

    @Test
    public void ensureAllPrefixesPresent_missingOne_throwsParseException() {
        ArgumentMultimap map = new ArgumentMultimap();
        Prefix p1 = new Prefix("p1/");
        Prefix p2 = new Prefix("p2/");
        map.put(p1, "value");

        // EP: One prefix missing out of multiple required
        assertThrows(ParseException.class, () ->
                ParserValidators.ensureAllPrefixesPresent(map,
                        new Prefix[]{p1, p2}, new String[]{"p1/", "p2/"}, USAGE));
    }

    @Test
    public void ensureIndexAndPrefixesPresent_missingBoth_correctErrorMessage() {
        ArgumentMultimap map = new ArgumentMultimap(); // Empty preamble and empty map
        Prefix p1 = new Prefix("crs/");

        // EP: Both index and prefix missing. Verifies the " AND " joining logic.
        String expectedMessage = "Missing required: student index AND prefix(es): crs/\n" + USAGE;

        try {
            ParserValidators.ensureIndexAndPrefixesPresent(map, new Prefix[]{p1}, new String[]{"crs/"}, USAGE);
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }
}
