package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Collections;
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
import seedu.address.model.person.Remark;
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

    //=========== Cancel/Uncancel Week =============================================================
    /**
     * Returns {@code true} if there exists at least one {@code Person} in the model
     * with the specified course ID and tutorial group.
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @return {@code true} if a matching course–tutorial pair exists, {@code false} otherwise.
     */
    @Override
    public boolean hasCourseTGroup(CourseId courseId, TGroup tGroup) {
        ObservableList<Person> persons = addressBook.getPersonList();
        return persons.stream()
                .anyMatch(p -> p.getCourseId().equals(courseId)
                        && p.getTGroup().equals(tGroup));
    }
    /**
     * Returns {@code true} if the specified week is marked as cancelled
     * for the given course ID and tutorial group.
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @param weekIdx The week index (0-based).
     * @return {@code true} if the week is cancelled, {@code false} otherwise.
     */
    @Override
    public boolean isWeekCancelled(CourseId courseId, TGroup tGroup, int weekIdx) {
        return getCancelledWeeks(courseId, tGroup).contains(weekIdx);
    }
    /**
     * Returns an unmodifiable set of cancelled week indices for the specified
     * course ID and tutorial group.
     *
     * <p>If no cancelled weeks exist for the given pair, an empty set is returned.
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @return An unmodifiable {@code Set} of cancelled week indices (0-based).
     * @throws NullPointerException if any argument is {@code null}.
     */
    @Override
    public Set<Integer> getCancelledWeeks(CourseId courseId, TGroup tGroup) {
        requireAllNonNull(courseId, tGroup);
        String key = makeKey(courseId, tGroup);
        return Collections.unmodifiableSet(
                cancelledWeeksMap.getOrDefault(key, Collections.emptySet()));
    }
    private void deriveCancelledWeekList(CourseId courseId, TGroup tGroup, int weekIndex) {
        List<Person> persons = addressBook.getPersonList();
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

                Person editedPerson = cloneWithWeekList(personToEdit, weekList);
                setPerson(personToEdit, editedPerson);
            }
        }
    }
    private void deriveUncancelledWeekList(CourseId courseId, TGroup tGroup, int weekIndex) {
        List<Person> persons = addressBook.getPersonList();
        // update existing persons
        for (Person personToEdit : persons) {
            if (personToEdit.getCourseId().equals(courseId)
                    && personToEdit.getTGroup().equals(tGroup)) {

                WeekList weekList = personToEdit
                        .getWeekList().copy();
                try {
                    weekList.markAsUncancelled(weekIndex);
                } catch (IllegalStateException e) {
                    continue; // skip invalid ones safely
                }

                Person updated = cloneWithWeekList(personToEdit, weekList);

                setPerson(personToEdit, updated);
            }
        }
    }
    private Person cloneWithWeekList(Person p, WeekList list) {
        Person updated = new Person(
                p.getName(),
                p.getCourseId(),
                p.getEmail(),
                p.getStudentId(),
                p.getTGroup(),
                p.getTele(),
                list,
                p.getProgress()
        );

        for (Remark remark : p.getRemarks()) {
            updated.addRemark(remark);
        }

        return updated;
    }

    /**
     * Adds a cancelled week for the specified course ID and tutorial group.
     *
     * <p>This method:
     * <ul>
     *     <li>Ensures the course–tutorial pair exists in the internal map</li>
     *     <li>Ignores the operation if the week is already marked as cancelled</li>
     *     <li>Propagates the cancellation to all matching {@code Person} objects</li>
     *     <li>Updates the persistent cancelled weeks map in the address book</li>
     * </ul>
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @param weekIndex The week index to cancel (0-based).
     * @throws NullPointerException if {@code courseId} or {@code tGroup} is {@code null}.
     */
    @Override
    public void addCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {
        requireAllNonNull(courseId, tGroup);
        String key = makeKey(courseId, tGroup);
        cancelledWeeksMap.putIfAbsent(key, new java.util.HashSet<>());
        if (cancelledWeeksMap.get(key).contains(weekIndex)) {
            return;
        }
        deriveCancelledWeekList(courseId, tGroup, weekIndex);
        cancelledWeeksMap.get(key).add(weekIndex);
        addressBook.getCancelledWeeksMap().putAll(cancelledWeeksMap); // to save
    }

    /**
     * Removes a cancelled week for the specified course ID and tutorial group.
     *
     * <p>This method:
     * <ul>
     *     <li>Does nothing if the course–tutorial pair or week does not exist</li>
     *     <li>Removes the week from the cancelled weeks map</li>
     *     <li>Propagates the uncancellation to all matching {@code Person} objects</li>
     *     <li>Updates the persistent cancelled weeks map in the address book</li>
     * </ul>
     *
     * @param courseId The course identifier.
     * @param tGroup The tutorial group.
     * @param weekIndex The week index to uncancel (0-based).
     * @throws NullPointerException if {@code courseId} or {@code tGroup} is {@code null}.
     */
    @Override
    public void removeCancelledWeek(CourseId courseId, TGroup tGroup, int weekIndex) {
        requireAllNonNull(courseId, tGroup);
        String key = makeKey(courseId, tGroup);
        if (!cancelledWeeksMap.containsKey(key)
                || !cancelledWeeksMap.get(key).contains(weekIndex)) {
            return;
        }
        cancelledWeeksMap.get(key).remove(weekIndex);
        deriveUncancelledWeekList(courseId, tGroup, weekIndex);
        addressBook.getCancelledWeeksMap().putAll(cancelledWeeksMap);
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

        Person updatedPerson = cloneWithWeekList(person, weekList);

        return updatedPerson;
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
