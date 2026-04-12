package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;

/**
 * Integration tests for {@code FilterCommand} checking interaction with the Model.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterCommand(null));
    }

    @Test
    public void execute_filterByCourse_success() {
        // EP: Valid Course ID exists in model
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(BENSON.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertFilterCommandSuccess(predicate);
        assertEquals(3, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_filterByCourse_caseInsensitiveMatch() {
        // EP: Case-insensitive match (Input lowercase, Model uppercase)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId(BENSON.getCourseId().value.toLowerCase())),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByAbsenceCount_boundaryValueZero() {
        // BVA: Boundary value 0 (Minimum valid absence)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(0));
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByCourseAndTGroup_success() {
        // EP: Multiple fields combined (AND logic)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(BENSON.getCourseId()),
                Optional.of(BENSON.getTGroup()), Optional.empty(),
                Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterByMultipleFields_success() {
        // EP: Multiple fields combined (AND logic) - TGroup and Progress
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(),
                Optional.of(BENSON.getTGroup()),
                Optional.of(BENSON.getProgress()),
                Optional.empty());
        assertFilterCommandSuccess(predicate);
    }

    @Test
    public void execute_filterNoMatches_success() {
        // EP: Resulting list is empty
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("NONEXISTENT123")),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertFilterCommandSuccess(predicate);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
    @Test
    public void equals() {
        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand firstCommand = new FilterCommand(firstPredicate);

        // same object -> true
        assertEquals(firstCommand, firstCommand);

        // same values -> true
        FilterMatchesPredicate samePredicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand sameCommand = new FilterCommand(samePredicate);
        assertEquals(firstCommand, sameCommand);

        // different values -> false
        FilterMatchesPredicate differentPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS1101S")),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand differentCommand = new FilterCommand(differentPredicate);
        assertNotEquals(firstCommand, differentCommand);

        // null -> false
        assertNotEquals(firstCommand, null);

        // different type -> false
        assertNotEquals(firstCommand, 5);
    }

    @Test
    public void toString_method() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(ALICE.getCourseId()),
                Optional.empty(), Optional.empty(), Optional.empty());
        FilterCommand command = new FilterCommand(predicate);

        String result = command.toString();

        assertTrue(result.contains("predicate"));
        assertTrue(result.contains(predicate.toString()));
    }
    private void assertFilterCommandSuccess(FilterMatchesPredicate predicate) {
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        AddressBook originalAddressBook = new AddressBook(model.getAddressBook());

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedModel.getFilteredPersonList().size()),
                expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
        assertEquals(originalAddressBook, model.getAddressBook());
    }
}
