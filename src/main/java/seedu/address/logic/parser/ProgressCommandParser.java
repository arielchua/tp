package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Progress;

/**
 * Parses input arguments and creates a new ProgressCommand object.
 */
public class ProgressCommandParser implements Parser<ProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ProgressCommand
     * and returns a ProgressCommand object for execution.
     *
     * @throws ParseException if the user input does not follow the expected format
     */
    public ProgressCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROGRESS);

        Prefix[] allowedPrefixes = {PREFIX_PROGRESS};

        ParserValidators.checkForBarePrefixes(argMultimap, allowedPrefixes, ProgressCommand.MESSAGE_USAGE);
        ParserValidators.checkForUnknownPrefixTokens(args, allowedPrefixes,
                "p/", ProgressCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PROGRESS);

        if (argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_PROGRESS).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
        }

        String preamble = argMultimap.getPreamble().trim();

        if (!preamble.matches("\\d+")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProgressCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(preamble);
        Progress progress = ParserUtil.parseProgress(argMultimap.getValue(PREFIX_PROGRESS).get());

        return new ProgressCommand(index, progress);
    }
}
