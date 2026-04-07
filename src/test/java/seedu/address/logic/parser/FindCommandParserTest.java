package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Tests the FindCommandParser. Integration with ParserUtil is verified here,
 * while specific regex validation is tested in ParserUtilTest.
 */
public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                ParserUtil.MESSAGE_EMPTY_KEYWORDS + "\n" + FindCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "Alice123",
                ParserUtil.MESSAGE_INVALID_KEYWORDS + "\n" + FindCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);
    }

    @Test
    public void parse_paddedArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice")));

        assertParseSuccess(parser, "Alice", expectedFindCommand);
    }
}
