package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;

/**
 * Extensive tests for shared structural validation logic in ParserValidators.
 * These tests ensure that the general "rules of the road" for parsers are enforced.
 */
public class ParserValidatorsTest {
    private static final String USAGE = FilterCommand.MESSAGE_USAGE;
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void checkForBarePrefixes_singleBarePrefix_throwsParseException() {
        assertParseFailure(parser, " abs 5",
                ParserMessages.possiblePrefixMissingSlash(USAGE));
    }

    @Test
    public void checkForBarePrefixes_multipleBarePrefixes_throwsParseException() {
        // Multiple tokens in preamble that look like prefixes without slashes
        assertParseFailure(parser, " abs crs tg p",
                ParserMessages.possiblePrefixMissingSlash(USAGE));
    }

    @Test
    public void checkForBarePrefixes_caseInsensitiveBarePrefix_throwsParseException() {
        // Testing that "ABS" (uppercase) is caught just like "abs"
        assertParseFailure(parser, " ABS 5",
                ParserMessages.possiblePrefixMissingSlash(USAGE));
    }

    @Test
    public void checkForUnknownPrefixTokens_prefixWithValues_throwsParseException() {
        // An unknown prefix 'v/' buried among valid ones
        assertParseFailure(parser, " crs/CS2103T v/Unknown tg/T01",
                ParserMessages.invalidPrefix("crs/, tg/, p/, and abs/", USAGE));
    }

    @Test
    public void checkForUnknownPrefixTokens_standaloneSlash_throwsParseException() {
        // A token that is just a slash or has an unknown prefix
        assertParseFailure(parser, " /invalid",
                ParserMessages.invalidPrefix("crs/, tg/, p/, and abs/", USAGE));
    }

    @Test
    public void checkForUnexpectedPreamble_preambleWithSpaces_throwsParseException() {
        assertParseFailure(parser, " This is a preamble crs/CS2103T",
                ParserMessages.unexpectedPreamble(USAGE));
    }

    @Test
    public void checkForMissingValues_multipleEmptyPrefixes_throwsFirstParseException() {
        // Should catch the first empty prefix encountered in the validation sequence (crs/)
        assertParseFailure(parser, " crs/ tg/ ",
                FilterCommandParser.MESSAGE_EMPTY_COURSE_ID);
    }

    @Test
    public void checkForMissingValues_mismatchedArrays_throwsIllegalArgumentException() {
        ArgumentMultimap map = new ArgumentMultimap();
        // Developer-facing safety check for the utility method itself
        assertThrows(IllegalArgumentException.class, () ->
                ParserValidators.checkForMissingValues(map,
                        new Prefix[]{new Prefix("a/")},
                        new String[]{}, // Empty array causes mismatch
                        new String[]{"Detail"}, USAGE));
    }
}
