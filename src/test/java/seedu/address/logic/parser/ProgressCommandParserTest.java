package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_PROGRESS;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ProgressCommand;
import seedu.address.model.person.Progress;

/**
 * Contains unit tests for {@code ProgressCommandParser}.
 */
public class ProgressCommandParserTest {

    private final ProgressCommandParser parser = new ProgressCommandParser();

    @Test
    public void parse_validIndexMode_success() {
        assertParseSuccess(parser,
                "1 p/on_track",
                new ProgressCommand(Index.fromOneBased(1), Progress.ON_TRACK));
    }

    @Test
    public void parse_validIndexModeClear_success() {
        assertParseSuccess(parser,
                "1 p/clear",
                new ProgressCommand(Index.fromOneBased(1), Progress.NOT_SET));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser,
                "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespace_failure() {
        assertParseFailure(parser,
                "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingProgress_failure() {
        assertParseFailure(parser,
                "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser,
                " p/on_track",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericPreamble_failure() {
        assertParseFailure(parser,
                "abc p/on_track",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_indexAndIdentityMixed_failure() {
        assertParseFailure(parser,
                "1 id/A0301200M crs/CS2103 tg/T01 p/on_track",
                ParserMessages.invalidPrefix("p/", ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser,
                "0 p/on_track",
                MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidProgress_failure() {
        assertParseFailure(parser,
                "1 p/bad_status",
                MESSAGE_INVALID_PROGRESS);
    }

    @Test
    public void parse_duplicateProgressPrefix_failure() {
        assertParseFailure(parser,
                "1 p/on_track p/at_risk",
                "Multiple values specified for the following single-valued field(s): p/");
    }

    @Test
    public void parse_barePrefix_failure() {
        assertParseFailure(parser,
                "1 p on_track",
                ParserMessages.possiblePrefixMissingSlash(ProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownPrefix_failure() {
        assertParseFailure(parser,
                "1 prog/on_track",
                ParserMessages.invalidPrefix("p/", ProgressCommand.MESSAGE_USAGE));
    }
}
