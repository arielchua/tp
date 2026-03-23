package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        // trim the input
        String trimmedInput = args.trim();
        if (trimmedInput.isEmpty()) {
            throw new ParseException(DeleteCommand.MESSAGE_EMPTY_INPUT
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        }

        if (trimmedInput.matches("[1-9]\\d*\\s+.+")) {
            throw new ParseException(DeleteCommand.MESSAGE_UNEXPECTED_TEXT_AFTER_INDEX
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        }

        if (trimmedInput.matches("[1-9]\\d*")) { // allows non-0 positive numbers
            try {
                Index index = ParserUtil.parseIndex(trimmedInput);
                return new DeleteCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        if (trimmedInput.matches("-?\\d+(\\.\\d+)?")) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_INDEX
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        } else {
            try {
                Name name = ParserUtil.parseName(trimmedInput);
                return new DeleteCommand(name);
            } catch (ParseException npe) {
                throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), npe);
            }
        }
    }
}
