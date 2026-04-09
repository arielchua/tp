package seedu.address.logic.commands;

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
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.Remark;
import seedu.address.model.person.TGroup;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Remark remark = new Remark("Good participation", LocalDate.of(2026, 3, 25));
        assertThrows(NullPointerException.class, () -> new RemarkCommand(null, remark));
    }

    @Test
    public void constructor_nullRemark_throwsNullPointerException() {
        Index index = Index.fromOneBased(1);
        assertThrows(NullPointerException.class, () -> new RemarkCommand(index, (Remark) null));
    }

    @Test
    public void execute_validIndex_addSuccessful() throws Exception {
        Person person = new PersonBuilder()
            .withName("Alice Pauling")
            .withCourseId("CS2103T")
            .withStudentId("A1234567A")
            .withTGroup("T01")
            .withTele("alice_pauling")
            .withEmail("alice@u.nus.edu")
            .withAbsences(0)
            .withProgress(Progress.ON_TRACK)
            .build();
        ModelStubWithPersonList modelStub = new ModelStubWithPersonList(person);

        Remark remark = new Remark("Good participation", LocalDate.of(2026, 3, 25));
        RemarkCommand command = new RemarkCommand(Index.fromOneBased(1), remark);

        CommandResult commandResult = command.execute(modelStub);
        Person updatedPerson = modelStub.getFilteredPersonList().get(0);

        assertEquals(String.format(RemarkCommand.MESSAGE_ADD_REMARKS_SUCCESS,
            Messages.format(updatedPerson) + "\n"
                + "Remark: " + remark.getText()), commandResult.getFeedbackToUser());
        assertEquals(1, updatedPerson.getRemarks().size());
        assertEquals(remark, updatedPerson.getRemarks().get(0));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Person person = new PersonBuilder().build();
        ModelStubWithPersonList modelStub = new ModelStubWithPersonList(person);

        Remark remark = new Remark("Good participation", LocalDate.of(2026, 3, 25));
        RemarkCommand command = new RemarkCommand(Index.fromOneBased(2), remark);

        assertThrows(CommandException.class,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        Remark firstRemark = new Remark("Good participation", LocalDate.of(2026, 3, 25));
        Remark secondRemark = new Remark("Needs improvement", LocalDate.of(2026, 3, 26));

        RemarkCommand firstCommand = new RemarkCommand(Index.fromOneBased(1), firstRemark);
        RemarkCommand firstCommandCopy = new RemarkCommand(Index.fromOneBased(1), firstRemark);
        RemarkCommand secondCommand = new RemarkCommand(Index.fromOneBased(2), secondRemark);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different command -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    /**
     * A default model stub that has all methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void removeCancelledWeek(CourseId courseId, TGroup tGroup, int weekNumber) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFullPersonList() {
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
        public void updateSortedPersonList(Comparator<Person> comparator, Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasCourseTGroup(CourseId courseId, TGroup tGroup) {
            return false;
        }

        @Override
        public boolean isWeekCancelled(CourseId courseId, TGroup tGroup, int weekIdx) {
            return false;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a mutable filtered person list.
     */
    private class ModelStubWithPersonList extends ModelStub {
        private final ObservableList<Person> filteredPersons;

        ModelStubWithPersonList(Person... persons) {
            filteredPersons = FXCollections.observableArrayList(persons);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return filteredPersons;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int index = filteredPersons.indexOf(target);
            filteredPersons.set(index, editedPerson);
        }
    }
}
