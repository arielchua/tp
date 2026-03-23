package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new CourseId("CS2103T"), new Email("alexyeoh@example.com"),
                new StudentId("A1234567X"), new TGroup("T12"), new Tele("@alexyeoh"), Progress.ON_TRACK),
            new Person(new Name("Bernice Yu"), new CourseId("CS2103T"), new Email("berniceyu@example.com"),
                new StudentId("A7654321X"), new TGroup("T12"), new Tele("@berniceyu"), Progress.NEEDS_ATTENTION),
            new Person(new Name("Charlotte Oliveiro"), new CourseId("CS2103T"), new Email("charlotte@example.com"),
                new StudentId("A1111111X"), new TGroup("T12"), new Tele("@charlotte"), Progress.NOT_SET),
            new Person(new Name("David Li"), new CourseId("CS2103T"), new Email("lidavid@example.com"),
                new StudentId("A2222222X"), new TGroup("T12"), new Tele("@lidavid"), Progress.NOT_SET),
            new Person(new Name("Irfan Ibrahim"), new CourseId("CS2103T"), new Email("irfan@example.com"),
                new StudentId("A3333333X"), new TGroup("T12"), new Tele("@irfan"), Progress.AT_RISK),
            new Person(new Name("Roy Balakrishnan"), new CourseId("CS2103T"), new Email("royb@example.com"),
                new StudentId("A4444444X"), new TGroup("T12"), new Tele("@royb"), Progress.NEEDS_ATTENTION)
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }
}
