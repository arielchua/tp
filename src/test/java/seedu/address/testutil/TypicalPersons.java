package seedu.address.testutil;

// import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_BOB;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

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
                                                        .build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
                                                        .withCourseId("CS2101")
                                                        .withStudentId("A1234567F")
                                                        .withTGroup("T09")
                                                        .withEmail("johnd@example.com")
                                                        .withTele("johndoe")
                                                        .build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
                                                        .withTele("carl_kurz")
                                                        .withEmail("heinz@example.com")
                                                        .withCourseId("CS1101S")
                                                        .withStudentId("A1234567H")
                                                        .withTGroup("T01")
                                                        .build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
                                                        .withTele("daniel_meier")
                                                        .withEmail("cornelia@example.com")
                                                        .withCourseId("HSS1000")
                                                        .withStudentId("A1234567I")
                                                        .withTGroup("T02")
                                                        .build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
                                                        .withTele("elle_meyer")
                                                        .withEmail("werner@example.com")
                                                        .withCourseId("GEN2061X")
                                                        .withStudentId("A1234567J")
                                                        .withTGroup("T03")
                                                        .build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
                                                        .withTele("fiona_kunz")
                                                        .withEmail("lydia@example.com")
                                                        .withCourseId("CS2103T")
                                                        .withStudentId("A1234567K")
                                                        .withTGroup("T04")
                                                        .build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
                                                        .withTele("george_best")
                                                        .withEmail("anna@example.com")
                                                        .withCourseId("CS2101")
                                                        .withStudentId("A1234567L")
                                                        .withTGroup("T05")
                                                        .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier")
                                                        .withTele("hoon_meier")
                                                        .withEmail("stefan@example.com")
                                                        .withCourseId("CS2103T")
                                                        .withStudentId("A1234567M")
                                                        .withTGroup("T06")
                                                        .build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller")
                                                        .withTele("ida_mueller")
                                                        .withEmail("hans@example.com")
                                                        .withCourseId("CS2101")
                                                        .withStudentId("A1234567N")
                                                        .withTGroup("T07")
                                                        .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY)
                                                        .withCourseId("CS2102")
                                                        .withStudentId("A1234567O")
                                                        .withTGroup("T10")
                                                        .withTele(VALID_TELE_AMY)
                                                        .withEmail(VALID_EMAIL_AMY)
                                                        .build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
                                                        .withCourseId("CS2102")
                                                        .withStudentId("A1234567P")
                                                        .withTGroup("T11")
                                                        .withTele(VALID_TELE_BOB)
                                                        .withEmail(VALID_EMAIL_BOB)
                                                        .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

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
