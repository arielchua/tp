package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private final RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_remarkContainingPrefixText_returnsSuccess() {
        Remark remark = new Remark("See txt/notes", LocalDate.now());
        assertParseSuccess(parser, "1 txt/See txt/notes",
                new RemarkCommand(Index.fromOneBased(1), remark));
    }

    @Test
    public void parse_longRemark_returnsFailure() {
        assertParseFailure(parser, "1 txt/" + "a".repeat(101),
                Remark.MESSAGE_TEXT_CONSTRAINTS);
    }

    @Test
    public void parse_emptyArgs_returnsFailure() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespaceArgs_returnsFailure() {
        assertParseFailure(parser, "    ",
             String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argumentsWithMissingPrefix_returnsFailure() {
        assertParseFailure(parser, "1 Participates actively in class",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_returnsFailure() {
        assertParseFailure(parser, "1 r/Participates actively in class",
            ParserMessages.invalidPrefix("txt/", RemarkCommand.MESSAGE_USAGE)
        );
    }

    @Test
    public void parse_missingRemarkAfterPrefix_returnsFailure() {
        assertParseFailure(parser, "1 txt/",
            ParserMessages.missingPrefixValue(
                "txt/",
                "Remark text cannot be empty.",
                RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_longRemark_returnsFailure() {
        assertParseFailure(parser, "1 txt/" + "a".repeat(101),
<<<<<<< HEAD
            "Remark is too long. Maximum length is 100 characters.");
=======
            Remark.MESSAGE_TEXT_CONSTRAINTS);
>>>>>>> b92e21ee (Fix parser validation)
    }

    @Test
    public void parse_missingIndex_returnsFailure() {
        assertParseFailure(parser, " txt/Participates actively in class",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroIndex_returnsFailure() {
        assertParseFailure(parser, "0 txt/Participates actively in class", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_negativeIndex_returnsFailure() {
        assertParseFailure(parser, "-1 txt/Participates actively in class", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_nonNumericIndex_returnsFailure() {
        assertParseFailure(parser, "abc txt/Participates actively in class", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_validArguments_returnsSuccess() {
        Remark remark = new Remark("Participates actively in class", LocalDate.now());
        assertParseSuccess(parser, "1 txt/Participates actively in class",
            new RemarkCommand(Index.fromOneBased(1), remark));
    }

    @Test
    public void parse_argumentsWithExtraWhitespace_returnsSuccess() {
        Remark remark = new Remark("Participates actively in class", LocalDate.now());
        assertParseSuccess(parser, "   1   txt/   Participates actively in class   ",
            new RemarkCommand(Index.fromOneBased(1), remark));
    }

    @Test
    public void parse_remarkWithPunctuation_returnsSuccess() {
        Remark remark = new Remark("Participates actively in class!", LocalDate.now());
        assertParseSuccess(parser, "1 txt/Participates actively in class!",
            new RemarkCommand(Index.fromOneBased(1), remark));
    }
}
