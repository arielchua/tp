package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private static final String MESSAGE_INVALID_KEYWORDS =
            "Keywords should contain alphabetic characters separated by spaces only.";

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                FindCommandParser.MESSAGE_EMPTY_KEYWORDS + "\n" + FindCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidCharacters_throwsParseException() {
        assertParseFailure(parser, "Alice1", MESSAGE_INVALID_KEYWORDS);
        assertParseFailure(parser, "Alice Bob3", MESSAGE_INVALID_KEYWORDS);
        assertParseFailure(parser, "Alice-Bob", MESSAGE_INVALID_KEYWORDS);
        assertParseFailure(parser, "Alice_Bob", MESSAGE_INVALID_KEYWORDS);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice")));

        assertParseSuccess(parser, "Alice", expectedFindCommand);
        assertParseSuccess(parser, "   Alice   ", expectedFindCommand);
    }

    @Test
    public void parse_mixedCaseKeywords_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("aLiCe", "bOB")));

        assertParseSuccess(parser, "aLiCe bOB", expectedFindCommand);
    }
}
