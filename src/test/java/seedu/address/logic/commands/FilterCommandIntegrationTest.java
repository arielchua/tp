package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;
import seedu.address.testutil.PersonBuilder;



/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add known persons so filtering conditions are deterministic
        model.addPerson(new PersonBuilder()
                .withName("Alice Filter")
                .withStudentId("A1111111A")
                .withEmail("alice@u.nus.edu")
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build());

        model.addPerson(new PersonBuilder()
                .withName("Bob Filter")
                .withStudentId("A2222222B")
                .withEmail("bob@u.nus.edu")
                .withCourseId("CS2103T")
                .withTGroup("T02")
                .withProgress(Progress.valueOf("AT_RISK"))
                .build());

        model.addPerson(new PersonBuilder()
                .withName("Cara Filter")
                .withStudentId("A3333333C")
                .withEmail("cara@u.nus.edu")
                .withCourseId("CS2101")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("NEEDS_ATTENTION"))
                .build());
    }

    @Test
    public void execute_filterByCourse_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.empty(),
                Optional.empty());

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int expectedCount = expectedModel.getFilteredPersonList().size();

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedCount),
                expectedModel);
    }

    @Test
    public void execute_filterByTGroup_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(),
                Optional.of(new TGroup("T01")),
                Optional.empty());

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int expectedCount = expectedModel.getFilteredPersonList().size();

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedCount),
                expectedModel);
    }

    @Test
    public void execute_filterByProgress_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(),
                Optional.empty(),
                Optional.of(Progress.ON_TRACK));

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int expectedCount = expectedModel.getFilteredPersonList().size();

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedCount),
                expectedModel);
    }

    @Test
    public void execute_filterByCourseAndTGroup_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.empty());

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int expectedCount = expectedModel.getFilteredPersonList().size();

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedCount),
                expectedModel);
    }

    @Test
    public void execute_filterByAllFields_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK));

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int expectedCount = expectedModel.getFilteredPersonList().size();

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, expectedCount),
                expectedModel);
    }

    @Test
    public void execute_filterNoMatches_success() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS9999")),
                Optional.empty(),
                Optional.empty());

        FilterCommand command = new FilterCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model,
                String.format(FilterCommand.MESSAGE_SUCCESS, 0),
                expectedModel);
    }
}
