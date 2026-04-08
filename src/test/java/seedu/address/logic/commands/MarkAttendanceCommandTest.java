package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Week;
import seedu.address.model.person.WeekList;
import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for {@link MarkAttendanceCommand} using a ModelStub with ObservableList.
 */
public class MarkAttendanceCommandTest {

    private Person alice;

    @BeforeEach
    public void setUp() {
        alice = new PersonBuilder().withName("Alice").build();
    }

    /**
     * A simple Model stub using ObservableList for persons.
     */
    private static class ModelStub implements Model {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        public ModelStub(Person... persons) {
            for (Person p : persons) {
                this.persons.add(p);
            }
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public boolean isWeekCancelled(CourseId courseId, TGroup tGroup, int weekIdx) {
            return false;
        }

        @Override
        public Set<Integer> getCancelledWeeks(CourseId courseId, TGroup tGroup) {
            return Set.of();
        }

        @Override
        public void addCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {

        }

        @Override
        public void removeCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {

        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator, Predicate<Person> predicateShowAllPersons) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int index = persons.indexOf(target);
            if (index >= 0) {
                persons.set(index, editedPerson);
            }
        }

        // Unused methods throw UnsupportedOperationException
        @Override public void addPerson(Person person) {
            throw new UnsupportedOperationException(); }
        @Override public void deletePerson(Person person) {
            throw new UnsupportedOperationException(); }
        @Override public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new UnsupportedOperationException(); }
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }
        @Override public seedu.address.model.UserPrefs getUserPrefs() {
            throw new UnsupportedOperationException(); }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {

        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {

        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {

        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }
    }

    @Test
    public void execute_validMark_success() throws Exception {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        MarkAttendanceCommand cmd =
                new MarkAttendanceCommand(Index.fromOneBased(1),
                        Index.fromOneBased(1),
                        Week.Status.Y);

        CommandResult result = cmd.execute(model);

        Person updated = model.getFilteredPersonList().get(0);
        assertEquals(Week.Status.Y,
                updated.getWeekList().getWeek(0).getStatus());

        assertTrue(result.getFeedbackToUser().contains("marked as Y"));
    }

    @Test
    public void execute_invalidIndex_throwsException() {
        Model model = new ModelManager();

        MarkAttendanceCommand cmd =
                new MarkAttendanceCommand(Index.fromOneBased(1),
                        Index.fromOneBased(1),
                        Week.Status.Y);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_invalidWeek_throwsException() {
        Model model = new ModelManager();
        model.addPerson(new PersonBuilder().build());

        MarkAttendanceCommand cmd =
                new MarkAttendanceCommand(Index.fromOneBased(1),
                        Index.fromOneBased(20),
                        Week.Status.Y);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_duplicateStatus_throwsException() throws Exception {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        // First mark
        new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1),
                Week.Status.Y).execute(model);

        // Duplicate
        MarkAttendanceCommand cmd =
                new MarkAttendanceCommand(Index.fromOneBased(1),
                        Index.fromOneBased(1),
                        Week.Status.Y);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_markWeekAttended_success() throws CommandException {
        ModelStub model = new ModelStub(alice);
        MarkAttendanceCommand command = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.Y);
        command.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(0);
        WeekList weekList = editedPerson.getWeekList();
        assertEquals("Y", ((WeekList) weekList).getWeek(0).getStatus().toString());
    }

    @Test
    public void execute_markWeekAbsent_success() throws CommandException {
        ModelStub model = new ModelStub(alice);
        MarkAttendanceCommand command = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(2), Week.Status.A);
        command.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(0);
        WeekList weekList = editedPerson.getWeekList();
        assertEquals("A", ((WeekList) weekList).getWeek(1).getStatus().toString());
    }

    @Test
    public void execute_markWeekDefault_success() throws CommandException {
        ModelStub model = new ModelStub(alice);

        // First mark attended
        MarkAttendanceCommand commandY = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(3), Week.Status.Y);
        commandY.execute(model);

        // Reset to default
        MarkAttendanceCommand commandN = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(3), Week.Status.N);
        commandN.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(0);
        WeekList weekList = editedPerson.getWeekList();
        assertEquals("N", ((WeekList) weekList).getWeek(2).getStatus().toString());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ModelStub model = new ModelStub(alice);
        MarkAttendanceCommand command = new MarkAttendanceCommand(Index.fromOneBased(2),
                Index.fromOneBased(1), Week.Status.Y);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_duplicateStatus_throwsCommandException() throws CommandException {
        ModelStub model = new ModelStub(alice);
        // Mark attended
        MarkAttendanceCommand commandY = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.Y);
        commandY.execute(model);

        // Mark attended again
        MarkAttendanceCommand duplicateCommand = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.Y);

        assertThrows(CommandException.class, () -> duplicateCommand.execute(model));
    }

    @Test
    public void equals() {
        MarkAttendanceCommand command1 = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.Y);
        MarkAttendanceCommand command2 = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.Y);
        MarkAttendanceCommand command3 = new MarkAttendanceCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), Week.Status.A);

        assertEquals(command1, command2);
        assert !command1.equals(command3);
        assert !command1.equals(null);
        assert !command1.equals(new Object());
    }
}
