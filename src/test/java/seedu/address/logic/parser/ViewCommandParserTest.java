package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
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
        // Non-numeric input
        assertParseFailure(parser, "a",
                ParserUtil.MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);

        // Zero index (AB3 indexes are 1-based)
        assertParseFailure(parser, "0",
                ParserUtil.MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);

        // Negative index
        assertParseFailure(parser, "-1",
                ParserUtil.MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);

        // Empty string or whitespace only
        assertParseFailure(parser, "",
                ParserUtil.MESSAGE_MISSING_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "   ",
                ParserUtil.MESSAGE_MISSING_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);
    }
}
