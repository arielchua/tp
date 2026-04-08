package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Week;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();
    private ModelManager model;
    private CourseId courseId;
    private TGroup tGroup;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        courseId = new CourseId("CS2103T");
        tGroup = new TGroup("T01");
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    // =========================
    // CORE BEHAVIOUR
    // =========================


    @Test
    public void addCancelledWeek_onlyAffectsMatchingCourseAndGroup() {
        Person match = new PersonBuilder().withCourseId("CS2103T").withTGroup("T01").build();
        Person nonMatch = new PersonBuilder().withCourseId("CS2040").withTGroup("T02").build();

        model.addPerson(match);
        model.addPerson(nonMatch);

        model.addCancelledWeek(courseId, tGroup, 0);

        List<Person> persons = model.getFilteredPersonList();

        assertEquals(Week.Status.C,
                persons.get(0).getWeekList().getWeek(0).getStatus());

        assertNotEquals(Week.Status.C,
                persons.get(1).getWeekList().getWeek(0).getStatus());
    }



    // =========================
    // EDGE CASES
    // =========================

    @Test
    public void addCancelledWeek_duplicateCall_noChange() {
        Person p = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build();

        model.addPerson(p);

        model.addCancelledWeek(courseId, tGroup, 0);
        model.addCancelledWeek(courseId, tGroup, 0);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 0));
    }

    @Test
    public void removeCancelledWeek_notCancelled_noCrash() {
        model.removeCancelledWeek(courseId, tGroup, 0);

        assertFalse(model.isWeekCancelled(courseId, tGroup, 0));
    }
    @Test
    public void addPerson_afterCancel_inheritsCancelledWeek() {
        model.addCancelledWeek(courseId, tGroup, 0);

        Person newPerson = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build();

        model.addPerson(newPerson);

        Person added = model.getFilteredPersonList().get(0);

        assertEquals(Week.Status.C,
                added.getWeekList().getWeek(0).getStatus());
    }

    // =========================
    // MULTI-WEEK
    // =========================

    @Test
    public void multipleWeeks_cancelAndUncancel_independent() {
        Person p = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build();

        model.addPerson(p);

        model.addCancelledWeek(courseId, tGroup, 0);
        model.addCancelledWeek(courseId, tGroup, 1);

        model.removeCancelledWeek(courseId, tGroup, 0);

        Person updated = model.getFilteredPersonList().get(0);

        assertNotEquals(Week.Status.C,
                updated.getWeekList().getWeek(0).getStatus());

        assertEquals(Week.Status.C,
                updated.getWeekList().getWeek(1).getStatus());
    }

    // =========================
    // KEY STRUCTURE BUG TEST
    // =========================

    @Test
    public void differentCourseSameTGroup_notAffected() {
        Person p1 = new PersonBuilder().withCourseId("CS2103T").withTGroup("T01").build();
        Person p2 = new PersonBuilder().withCourseId("CS2040").withTGroup("T01").build();

        model.addPerson(p1);
        model.addPerson(p2);

        model.addCancelledWeek(new CourseId("CS2103T"), new TGroup("T01"), 0);

        List<Person> persons = model.getFilteredPersonList();

        assertEquals(Week.Status.C,
                persons.get(0).getWeekList().getWeek(0).getStatus());

        assertNotEquals(Week.Status.C,
                persons.get(1).getWeekList().getWeek(0).getStatus());
    }
}
