package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests for {@code FindCommand}.
 * Verifies interaction between the command, predicate, and model.
 */
public class FindCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noKeywordsMatch_noPersonFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("zzz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0), expectedModel);
    }

    @Test
    public void execute_exactKeyword_multiplePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_prefixKeyword_multiplePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Ku");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_caseInsensitivePrefixKeyword_multiplePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("kU");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_multipleKeywords_somePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Ku El");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_duplicateKeywords_correctPersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate("Ku Ku");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_afterPreviousFiltering_replacesFilteredList() {
        NameContainsKeywordsPredicate firstPredicate = preparePredicate("Ku");
        model.updateFilteredPersonList(firstPredicate);

        NameContainsKeywordsPredicate secondPredicate = preparePredicate("El");
        FindCommand command = new FindCommand(secondPredicate);
        expectedModel.updateFilteredPersonList(secondPredicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Arrays.asList("first", "second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        assertEquals(findFirstCommand, findFirstCommand);
        assertEquals(findFirstCommand, new FindCommand(firstPredicate));

        assertNotEquals(null, findFirstCommand);
        assertNotEquals(1, findFirstCommand);
        assertNotEquals(findFirstCommand, findSecondCommand);
    }

    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        try {
            return new NameContainsKeywordsPredicate(ParserUtil.parseKeywords(userInput));
        } catch (ParseException e) {
            throw new AssertionError("Test input should be valid.");
        }
    }
}
