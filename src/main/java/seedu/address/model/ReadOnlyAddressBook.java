package seedu.address.model;

import java.util.Map;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();
    Map<String, Set<Integer>> getCancelledWeeksMap();
    void setCancelledWeeksMap(Map<String, Set<Integer>> freshCanceledWeekMap);

}
