package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.Remark;
import seedu.address.model.person.TGroup;
import seedu.address.testutil.PersonBuilder;

public class UnremarkCommandTest {

    // @Test
    public void constructor_nullTargetIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnremarkCommand(null, Index.fromOneBased(1)));
    }

    @Test
    public void constructor_nullRemarkIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnremarkCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_validIndex_removeSuccessful() throws Exception {
        Person person = new PersonBuilder()
            .withName("Alice Pauline")
            .withCourseId("CS2103T")
            .withStudentId("A1234567A")
            .withTGroup("T01")
            .withTele("alice_pauline")
            .withEmail("E1384397@u.nus.edu")
            .withAbsences(0)
            .withProgress(Progress.ON_TRACK)
            .build();

        Remark remark = new Remark("Good participation", LocalDate.of(2026, 3, 25));
        person.addRemark(remark);

        ModelStubWithPersonList modelStub = new ModelStubWithPersonList(person);
        UnremarkCommand command = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(UnremarkCommand.MESSAGE_DELETE_REMARKS_SUCCESS, person),
                commandResult.getFeedbackToUser());
        assertEquals(0, person.getRemarks().size());
    }

    @Test
    public void execute_invalidTargetIndex_throwsCommandException() {
        Person person = new PersonBuilder()
            .withName("Alice Pauline")
            .withCourseId("CS2103T")
            .withStudentId("A1234567A")
            .withTGroup("T01")
            .withTele("alice_pauline")
            .withEmail("E1384397@u.nus.edu")
            .withAbsences(0)
            .withProgress(Progress.ON_TRACK)
            .build();

        ModelStubWithPersonList modelStub = new ModelStubWithPersonList(person);
        UnremarkCommand command = new UnremarkCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        assertThrows(CommandException.class,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(modelStub));
    }

    @Test
    public void execute_invalidRemarkIndex_throwsCommandException() {
        Person person = new PersonBuilder()
            .withName("Alice Pauline")
            .withCourseId("CS2103T")
            .withStudentId("A1234567A")
            .withTGroup("T01")
            .withTele("alice_pauline")
            .withEmail("E1384397@u.nus.edu")
            .withAbsences(0)
            .withProgress(Progress.ON_TRACK)
            .build();

        ModelStubWithPersonList modelStub = new ModelStubWithPersonList(person);
        UnremarkCommand command = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        assertThrows(CommandException.class,
            UnremarkCommand.MESSAGE_INVALID_REMARK_INDEX, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        UnremarkCommand unremarkFirstCommand = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        UnremarkCommand unremarkFirstCommandCopy = new UnremarkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        UnremarkCommand unremarkSecondCommand = new UnremarkCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(unremarkFirstCommand.equals(unremarkFirstCommand));

        // same values -> returns true
        assertTrue(unremarkFirstCommand.equals(unremarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(unremarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unremarkFirstCommand.equals(null));

        assertFalse(unremarkFirstCommand.equals(unremarkSecondCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void removeCancelledWeek(CourseId courseId, TGroup tGroup, int weekNumber) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addCancelledWeek(CourseId courseId, TGroup tGroup, int weekNumber) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Set<Integer> getCancelledWeeks(CourseId courseId, TGroup tGroup) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError();
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError();
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError();
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError();
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError();
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError();
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError();
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError();
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError();
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError();
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator, Predicate<Person> predicate) {
            throw new AssertionError();
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPersonList extends ModelStub {
        private final ObservableList<Person> filteredPersonList = FXCollections.observableArrayList();

        ModelStubWithPersonList(Person person) {
            requireNonNull(person);
            this.filteredPersonList.add(person);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return filteredPersonList;
        }
    }
}
