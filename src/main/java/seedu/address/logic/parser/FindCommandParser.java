package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {
    public static final String MESSAGE_INVALID_KEYWORDS =
            "Keywords should contain alphabetic characters separated by spaces only.";
    public static final String MESSAGE_EMPTY_KEYWORDS =
            "Find command requires at least one keyword.";
    private static final String KEYWORDS_VALIDATION_REGEX = "[A-Za-z]+(\\s+[A-Za-z]+)*";


    /**
     * * Parses the given {@code String} of arguments in the context of the {@code FindCommand}
     * and returns a {@code FindCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_KEYWORDS + "\n" + FindCommand.MESSAGE_USAGE);
        }

        if (!trimmedArgs.matches(KEYWORDS_VALIDATION_REGEX)) {
            throw new ParseException(MESSAGE_INVALID_KEYWORDS);
        }

        List<String> nameKeywords = Arrays.asList(trimmedArgs.split("\\s+"));

        assert !nameKeywords.isEmpty();

        return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
    }
}
