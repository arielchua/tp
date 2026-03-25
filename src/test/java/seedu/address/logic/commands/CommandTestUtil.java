package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Arrays;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    // ===================== Valid values =====================

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";

    public static final String VALID_EMAIL_AMY = "amy@u.nus.edu";
    public static final String VALID_EMAIL_BOB = "bob@u.nus.edu";

    public static final String VALID_STUDENT_ID_AMY = "A1234567X";
    public static final String VALID_STUDENT_ID_BOB = "A7654321X";

    public static final String VALID_COURSE_ID_AMY = "CS2103T";
    public static final String VALID_COURSE_ID_BOB = "CS2101";

    public static final String VALID_TGROUP_AMY = "T01";
    public static final String VALID_TGROUP_BOB = "T02";

    public static final String VALID_TELE_AMY = "amy_bee";
    public static final String VALID_TELE_BOB = "bob_choo";

    // ===================== Descriptor strings (prefix + value)
    // =====================

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;

    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;

    public static final String STUDENT_ID_DESC_AMY = " " + PREFIX_STUDENTID + VALID_STUDENT_ID_AMY;
    public static final String STUDENT_ID_DESC_BOB = " " + PREFIX_STUDENTID + VALID_STUDENT_ID_BOB;

    public static final String COURSE_ID_DESC_AMY = " " + PREFIX_COURSEID + VALID_COURSE_ID_AMY;
    public static final String COURSE_ID_DESC_BOB = " " + PREFIX_COURSEID + VALID_COURSE_ID_BOB;

    public static final String TGROUP_DESC_AMY = " " + PREFIX_TGROUP + VALID_TGROUP_AMY;
    public static final String TGROUP_DESC_BOB = " " + PREFIX_TGROUP + VALID_TGROUP_BOB;

    public static final String TELE_DESC_AMY = " " + PREFIX_TELE + VALID_TELE_AMY;
    public static final String TELE_DESC_BOB = " " + PREFIX_TELE + VALID_TELE_BOB;

    // ===================== Invalid descriptor strings =====================

    // Name: blank / starts with whitespace
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + " ";

    // Email: not an NUS email
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "notavalidemail";

    // StudentId: wrong format (not letter + 7 digits + letter)
    public static final String INVALID_STUDENT_ID_DESC = " " + PREFIX_STUDENTID + "12345678";

    // CourseId: contains invalid characters (depends on your CourseId validation)
    public static final String INVALID_COURSE_ID_DESC = " " + PREFIX_COURSEID + "$$INVALID$$";

    // TGroup: contains invalid characters
    public static final String INVALID_TGROUP_DESC = " " + PREFIX_TGROUP + "!!";

    // Tele: contains invalid characters (not alphanumeric/underscore)
    public static final String INVALID_TELE_DESC = " " + PREFIX_TELE + "invalid handle!";

    // ===================== Preamble strings =====================

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    // ===================== Command execution helpers =====================

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedMessage, result.getFeedbackToUser());
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@link CommandException} is thrown <br>
     * - the CommandException's message matches {@code expectedMessage} <br>
     * - the {@code actualModel} remains unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        Model modelCopy = actualModel;

        try {
            command.execute(actualModel);
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(modelCopy, actualModel);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given
     * {@code targetIndex} in the {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }
}
