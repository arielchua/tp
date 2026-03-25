package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewCommand;

public class ViewCommandParserTest {

    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_validArgs_returnsViewCommand() {
        assertParseSuccess(parser, "1", new ViewCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        // Test for the new custom "missing index" message
        assertParseFailure(parser, "   ",
                ViewCommandParser.MESSAGE_INDEX_MISSING + "\n" + ViewCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_nonNumericArgs_throwsParseException() {
        assertParseFailure(parser, "abc",
                ViewCommandParser.MESSAGE_NOT_A_NUMBER + "\n" + ViewCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, "1 2",
                ViewCommandParser.MESSAGE_TOO_MANY_ARGS + "\n" + ViewCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // ParserUtil.MESSAGE_INVALID_INDEX is "Index is not a non-zero unsigned integer."
        assertParseFailure(parser, "0",
                ParserUtil.MESSAGE_INVALID_INDEX + "\n" + ViewCommand.MESSAGE_USAGE);
    }
}
