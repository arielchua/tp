package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_MISSING_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_TOO_MANY_ARGUMENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewCommand;

/**
 * Contains unit tests for {@code ViewCommandParser}.
 */
public class ViewCommandParserTest {

    private final ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_validArgs_returnsViewCommand() {
        // No whitespace
        assertParseSuccess(parser, "1", new ViewCommand(INDEX_FIRST_PERSON));

        // Leading/trailing whitespace
        assertParseSuccess(parser, "  1  ", new ViewCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedInvalidIndexMessage = MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE;
        String expectedTooManyArgsMessage = MESSAGE_TOO_MANY_ARGUMENTS + "\n" + ViewCommand.MESSAGE_USAGE;

        // Non-numeric input
        assertParseFailure(parser, "a", expectedInvalidIndexMessage);

        // Zero index
        assertParseFailure(parser, "0", expectedInvalidIndexMessage);

        // Negative index
        assertParseFailure(parser, "-1", expectedInvalidIndexMessage);

        // Input larger than Integer.MAX_VALUE
        assertParseFailure(parser, "2147483648", expectedInvalidIndexMessage);

        // Too many arguments
        assertParseFailure(parser, "1 2", expectedTooManyArgsMessage);
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        String expectedMissingIndexMessage = MESSAGE_MISSING_INDEX + "\n" + ViewCommand.MESSAGE_USAGE;
        // Empty string
        assertParseFailure(parser, "", expectedMissingIndexMessage);

        // Whitespace only
        assertParseFailure(parser, "   ", expectedMissingIndexMessage);
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        // Null input
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_robustness_failure() {
        String expectedInvalidIndexMessage = MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE;
        // EP: Decimal value
        assertParseFailure(parser, "1.0", expectedInvalidIndexMessage);
    }
}
