package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    private static final Prefix[] ALLOWED_PREFIXES = {PREFIX_REMARK};
    private static final String ALLOWED_PREFIXES_HUMAN_READABLE = "txt/";

    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        String prefix = PREFIX_REMARK.toString();
        int prefixPosition = trimmedArgs.indexOf(prefix);

        if (prefixPosition == -1) {
            for (String token : trimmedArgs.split("\\s+")) {
                if (token.contains("/")) {
                    throw new ParseException(
                        ParserMessages.invalidPrefix(
                            ALLOWED_PREFIXES_HUMAN_READABLE,
                            RemarkCommand.MESSAGE_USAGE));
                }
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        String indexPart = trimmedArgs.substring(0, prefixPosition).trim();
        String remarkText = trimmedArgs.substring(prefixPosition + prefix.length()).trim();

        if (indexPart.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        if (remarkText.isEmpty()) {
            throw new ParseException(ParserMessages.missingPrefixValue(
                    "txt/",
                    "Remark text cannot be empty.",
                    RemarkCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(indexPart);

        if (!Remark.isValidText(remarkText)) {
            throw new ParseException(Remark.MESSAGE_TEXT_CONSTRAINTS);
        }

        Remark remark = new Remark(remarkText, LocalDate.now());
        return new RemarkCommand(index, remark);
    }
}
