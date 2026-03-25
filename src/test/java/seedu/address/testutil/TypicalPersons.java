package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withCourseId("CS2103T")
            .withStudentId("A1234567G")
            .withTGroup("T08")
            .withEmail("alice@example.com")
            .withTele("alice_pauline")
            .withProgress(Progress.ON_TRACK)
            .build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withCourseId("CS2101")
            .withStudentId("A1234567F")
            .withTGroup("T09")
            .withEmail("johnd@example.com")
            .withTele("johndoe")
            .withProgress(Progress.AT_RISK)
            .build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withTele("carl_kurz")
            .withEmail("heinz@example.com")
            .withCourseId("CS1101S")
            .withStudentId("A1234567H")
            .withTGroup("T01")
            .withProgress(Progress.NEEDS_ATTENTION)
            .build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withTele("daniel_meier")
            .withEmail("cornelia@example.com")
            .withCourseId("HSS1000")
            .withStudentId("A1234567I")
            .withTGroup("T02")
            .withProgress(Progress.ON_TRACK)
            .build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withTele("elle_meyer")
            .withEmail("werner@example.com")
            .withCourseId("GEN2061X")
            .withStudentId("A1234567J")
            .withTGroup("T03")
            .withProgress(Progress.ON_TRACK)
            .build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withTele("fiona_kunz")
            .withEmail("lydia@example.com")
            .withCourseId("CS2103T")
            .withStudentId("A1234567K")
            .withTGroup("T04")
            .withProgress(Progress.AT_RISK)
            .build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withTele("george_best")
            .withEmail("anna@example.com")
            .withCourseId("CS2101")
            .withStudentId("A1234567L")
            .withTGroup("T05")
            .withProgress(Progress.NEEDS_ATTENTION)
            .build();

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
