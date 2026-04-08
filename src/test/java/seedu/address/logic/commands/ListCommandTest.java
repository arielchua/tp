package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.testutil.PersonBuilder;


public class ListCommandTest {
    class ModelStub implements Model {

        private Comparator<Person> comparatorPassed;
        private Predicate<Person> predicatePassed;
        private boolean methodCalled = false;

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

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

        @Override
        public void deletePerson(Person target) {

        }

        @Override
        public void addPerson(Person person) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {

        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
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
        public void updateFilteredPersonList(Predicate<Person> predicate) {

        }
        @Override
        public void updateSortedPersonList(Comparator<Person> comparator,
                                           Predicate<Person> predicate) {
            methodCalled = true;
            comparatorPassed = comparator;
            predicatePassed = predicate;
        }
    }
    @Test
    public void execute_callsUpdateSortedPersonList() {
        ModelStub model = new ModelStub();

        new ListCommand().execute(model);

        assertTrue(model.methodCalled);
    }
    @Test
    public void execute_usesShowAllPredicate() {
        ModelStub model = new ModelStub();

        new ListCommand().execute(model);

        assertEquals(Model.PREDICATE_SHOW_ALL_PERSONS, model.predicatePassed);
    }

    @Test
    public void execute_usesCorrectComparator() {
        ModelStub model = new ModelStub();

        new ListCommand().execute(model);

        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();

        int result = model.comparatorPassed.compare(alice, bob);

        assertTrue(result < 0); // Alice should come before Bob
    }

    @Test
    public void execute_returnsCorrectMessage() {
        ModelStub model = new ModelStub();

        CommandResult result = new ListCommand().execute(model);

        assertEquals(ListCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_nullModel_throwsException() {
        ListCommand command = new ListCommand();

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }
}
