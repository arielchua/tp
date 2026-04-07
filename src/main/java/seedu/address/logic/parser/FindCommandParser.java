package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * * Parses the given {@code String} of arguments in the context of the {@code FindCommand}
     * and returns a {@code FindCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        try {
            List<String> keywords = ParserUtil.parseKeywords(args);
            return new FindCommand(new NameContainsKeywordsPredicate(keywords));
        } catch (ParseException pe) {

            throw new ParseException(pe.getMessage() + "\n" + FindCommand.MESSAGE_USAGE);
        }
    }
}
