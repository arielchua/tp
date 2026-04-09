package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnremarkCommand;

/**
 * Contains unit tests for {@code UnremarkCommandParser}.
 */
public class UnremarkCommandParserTest {

    private final UnremarkCommandParser parser = new UnremarkCommandParser();

    @Test
    public void parse_emptyArgs_returnsFailure() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespaceArgs_returnsFailure() {
        assertParseFailure(parser, "    ",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_missingTargetIndex_returnsFailure() {
        assertParseFailure(parser, " r/1",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroTargetIndex_returnsFailure() {
        assertParseFailure(parser, " 0 r/1", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_negativeTargetIndex_returnsFailure() {
        assertParseFailure(parser, " -1 r/1", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_nonNumericTargetIndex_returnsFailure() {
        assertParseFailure(parser, " abc r/1", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_missingPrefix_returnsFailure() {
        assertParseFailure(parser, " 1 1",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_returnsFailure() {
        assertParseFailure(parser, "1 txt/1",
            ParserMessages.invalidPrefix("r/", UnremarkCommand.MESSAGE_USAGE)
        );
    }

    @Test
    public void parse_missingRemarkIndex_returnsFailure() {
        assertParseFailure(parser, "1 r/",
            ParserMessages.missingPrefixValue(
                "r/",
                "Remark index cannot be empty.",
                UnremarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroRemarkIndex_returnsFailure() {
        assertParseFailure(parser, "1 r/0", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_negativeRemarkIndex_returnsFailure() {
        assertParseFailure(parser, "1 r/-1", ParserUtil.MESSAGE_INVALID_INDEX);

    }

    @Test
    public void parse_nonNumericRemarkIndex_returnsFailure() {
        assertParseFailure(parser, "1 r/abc", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_extraTrailingText_returnsFailure() {
        assertParseFailure(parser, "1 r/1 trailing text", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_unexpectedTextBeforePrefix_returnsFailure() {
        assertParseFailure(parser, "1 unexpectedText r/1", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_validArguments_returnsSuccess() {
        UnremarkCommand unremark = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        assertParseSuccess(parser, " 1 r/1", unremark);
    }

    @Test
    public void parse_argumentsWithExtraWhitespace_returnsSuccess() {
        UnremarkCommand unremark = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        assertParseSuccess(parser, " 1     r/    1   ", unremark);
    }
}
