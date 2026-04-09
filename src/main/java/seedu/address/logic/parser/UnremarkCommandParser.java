package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNREMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnremarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnremarkCommand object.
 */
public class UnremarkCommandParser implements Parser<UnremarkCommand> {

    private static final Prefix[] ALLOWED_PREFIXES = {PREFIX_UNREMARK};
    private static final String ALLOWED_PREFIXES_HUMAN_READABLE = "r/";

    /**
     * Parses the given {@code String} of arguments in the context of the UnremarkCommand
     * and returns an UnremarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public UnremarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserValidators.checkForUnknownPrefixTokens(
            args,
            ALLOWED_PREFIXES,
            ALLOWED_PREFIXES_HUMAN_READABLE,
            UnremarkCommand.MESSAGE_USAGE
        );

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_UNREMARK);

        if (argMultimap.getPreamble().isBlank() || argMultimap.getValue(PREFIX_UNREMARK).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremarkCommand.MESSAGE_USAGE));
        }

        ParserValidators.checkForMissingValues(
            argMultimap,
            ALLOWED_PREFIXES,
            new String[] {"r/"},
            new String[] {"Remark index cannot be empty."},
            UnremarkCommand.MESSAGE_USAGE);

        Index personIndex = ParserUtil.parseIndex(argMultimap.getPreamble());

        String remarkIndexString = argMultimap.getValue(PREFIX_UNREMARK).get().trim();

        Index remarkIndex = ParserUtil.parseIndex(remarkIndexString);

        return new UnremarkCommand(personIndex, remarkIndex);
    }
}
