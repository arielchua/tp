package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.WeekList;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedPersons;
    private final Map<String, Set<Integer>> cancelledWeeksMap;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        sortedPersons = new SortedList<>(filteredPersons);
        this.cancelledWeeksMap = new HashMap<>(this.addressBook.getCancelledWeeksMap());

    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        Person updatedPerson = applyCancelledWeeks(person);
        addressBook.addPerson(updatedPerson);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        Person updatedPerson = applyCancelledWeeks(editedPerson);
        addressBook.setPerson(target, updatedPerson);
    }

    @Override
    public Set<Integer> getCancelledWeeks(CourseId courseId, TGroup tGroup) {
        requireAllNonNull(courseId, tGroup);
        String key = makeKey(courseId, tGroup);
        return cancelledWeeksMap.getOrDefault(key, new java.util.HashSet<>());
    }
    private void deriveCancelledWeekList(CourseId courseId, TGroup tGroup, int weekIndex) {
        List<Person> persons = getFilteredPersonList();
        for (Person personToEdit : persons) {
            if (personToEdit.getCourseId().equals(courseId)
                    && personToEdit.getTGroup().equals(tGroup)) {

                WeekList weekList = personToEdit
                        .getWeekList().copy();

                try {
                    weekList.markAsCancelled(weekIndex);
                } catch (IllegalStateException e) {
                    // ignore duplicates
                    continue;
                }

                Person editedPerson = new Person(
                        personToEdit.getName(),
                        personToEdit.getCourseId(),
                        personToEdit.getEmail(),
                        personToEdit.getStudentId(),
                        personToEdit.getTGroup(),
                        personToEdit.getTele(),
                        weekList,
                        personToEdit.getProgress()
                );
                setPerson(personToEdit, editedPerson);
            }
        }
    }
    private void deriveUncancelledWeekList(CourseId courseId, TGroup tGroup, int weekIndex) {
        List<Person> persons = getFilteredPersonList();
        // update existing persons
        for (Person person : persons) {
            if (person.getCourseId().equals(courseId)
                    && person.getTGroup().equals(tGroup)) {

                WeekList weekList = person
                        .getWeekList().copy();
                weekList.markWeekAsDefault(weekIndex);

                Person updated = new Person(
                        person.getName(),
                        person.getCourseId(),
                        person.getEmail(),
                        person.getStudentId(),
                        person.getTGroup(),
                        person.getTele(),
                        weekList,
                        person.getProgress()
                );

                setPerson(person, updated);
            }
        }
    }

    @Override
    public void addCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {
        requireAllNonNull(courseId, tGroup);
        deriveCancelledWeekList(courseId, tGroup, weekIndex);
        String key = makeKey(courseId, tGroup);
        cancelledWeeksMap.putIfAbsent(key, new java.util.HashSet<>());
        cancelledWeeksMap.get(key).add(weekIndex);

        addressBook.getCancelledWeeksMap().putAll(cancelledWeeksMap); // to save
    }

    @Override
    public void removeCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {
        requireAllNonNull(courseId, tGroup);
        String key = makeKey(courseId, tGroup);
        if (cancelledWeeksMap.containsKey(key)) {
            cancelledWeeksMap.get(key).remove(weekIndex);
        }

        addressBook.getCancelledWeeksMap().putAll(cancelledWeeksMap);
        deriveUncancelledWeekList(courseId, tGroup, weekIndex);
    }

    private Person applyCancelledWeeks(Person person) {
        WeekList weekList = person
                .getWeekList().copy();
        Set<Integer> cancelledWeeks =
                getCancelledWeeks(person.getCourseId(), person.getTGroup());

        for (int week : cancelledWeeks) {
            try {
                weekList.markAsCancelled(week);
            } catch (IllegalStateException e) {
                // ignore duplicates
            }
        }

        return new Person(
                person.getName(),
                person.getCourseId(),
                person.getEmail(),
                person.getStudentId(),
                person.getTGroup(),
                person.getTele(),
                weekList,
                person.getProgress()
        );
    }

    private String makeKey(CourseId courseId, TGroup tGroup) {
        return courseId.toString() + "-" + tGroup.toString();
    }

    /**
     * Returns an unmodifiable sorted view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public void updateSortedPersonList(Comparator<Person> comparator, Predicate<Person> predicate) {
        requireNonNull(comparator);
        filteredPersons.setPredicate(predicate);
        sortedPersons.setComparator(comparator);
    }

    //=========== Filered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return sortedPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
        sortedPersons.setComparator(null);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }



}
