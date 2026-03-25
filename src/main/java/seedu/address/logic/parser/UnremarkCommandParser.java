package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnremarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnremarkCommand object.
 */
public class UnremarkCommandParser implements Parser<UnremarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnremarkCommand
     * and returns an UnremarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public UnremarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        if (argMultimap.getPreamble().isBlank() || argMultimap.getValue(PREFIX_REMARK).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(argMultimap.getPreamble());

        String remarkIndexString = argMultimap.getValue(PREFIX_REMARK).get().trim();
        if (remarkIndexString.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
        }

        Index remarkIndex = ParserUtil.parseIndex(remarkIndexString);

        return new UnremarkCommand(personIndex, remarkIndex);
    }
}
