package seedu.address.model.util;

import java.time.LocalDate;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.Remark;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;
import seedu.address.model.person.WeekList;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        Person alex = new Person(
            new Name("Alex Yeoh"),
            new CourseId("CS2103T"),
            new Email("alexyeoh@u.nus.edu"),
            new StudentId("A1234567X"),
            new TGroup("T12"),
            new Tele("@alexyeoh"),
            new WeekList(),
            Progress.ON_TRACK);
        alex.addRemark(new Remark("Participates actively in class", LocalDate.now()));

        Person bernice = new Person(
            new Name("Bernice Yu"),
            new CourseId("CS2103T"),
            new Email("berniceyu@u.nus.edu"),
            new StudentId("A7654321X"),
            new TGroup("T12"),
            new Tele("@berniceyu"),
            new WeekList(),
            Progress.NEEDS_ATTENTION);

        Person charlotte = new Person(
            new Name("Charlotte Oliveiro"),
            new CourseId("CS2103T"),
            new Email("charlotte@u.nus.edu"),
            new StudentId("A1111111X"),
            new TGroup("T12"),
            new Tele("@charlotte"),
            new WeekList(),
            Progress.NOT_SET);
        charlotte.addRemark(new Remark("Has not attended any tutorial sessions", LocalDate.now()));

        Person david = new Person(
            new Name("David Li"),
            new CourseId("CS2103T"),
            new Email("lidavid@u.nus.edu"),
            new StudentId("A2222222X"),
            new TGroup("T12"),
            new Tele("@lidavid"),
            new WeekList(),
            Progress.NOT_SET);

        Person irfan = new Person(
            new Name("Irfan Ibrahim"),
            new CourseId("CS2103T"),
            new Email("irfan@u.nus.edu"),
            new StudentId("A3333333X"),
            new TGroup("T12"),
            new Tele("@irfan"),
            new WeekList(),
            Progress.AT_RISK);
        irfan.addRemark(new Remark("Needs to improve participation", LocalDate.now()));

        Person roy = new Person(
            new Name("Roy Balakrishnan"),
            new CourseId("CS2103T"),
            new Email("royb@u.nus.edu"),
            new StudentId("A4444444X"),
            new TGroup("T12"),
            new Tele("@royb"),
            new WeekList(),
            Progress.NEEDS_ATTENTION);

        return new Person[] {
            new Person(new Name("Alex Yeoh"), new CourseId("CS2103T"), new Email("alexyeoh@u.nus.edu"),
                new StudentId("A1234567X"), new TGroup("T12"), new Tele("@alexyeoh"),
                    new WeekList(), Progress.ON_TRACK),
            new Person(new Name("Bernice Yu"), new CourseId("CS2103T"), new Email("berniceyu@u.nus.edu"),
                new StudentId("A7654321X"), new TGroup("T12"), new Tele("@berniceyu"),
                    new WeekList(), Progress.NEEDS_ATTENTION),
            new Person(new Name("Charlotte Oliveiro"), new CourseId("CS2103T"), new Email("charlotte@u.nus.edu"),
                new StudentId("A1111111X"), new TGroup("T12"), new Tele("@charlotte"),
                    new WeekList(), Progress.NOT_SET),
            new Person(new Name("David Li"), new CourseId("CS2103T"), new Email("lidavid@u.nus.edu"),
                new StudentId("A2222222X"), new TGroup("T12"), new Tele("@lidavid"),
                    new WeekList(), Progress.NOT_SET),
            new Person(new Name("Irfan Ibrahim"), new CourseId("CS2103T"), new Email("irfan@u.nus.edu"),
                new StudentId("A3333333X"), new TGroup("T12"), new Tele("@irfan"),
                    new WeekList(), Progress.AT_RISK),
            new Person(new Name("Roy Balakrishnan"), new CourseId("CS2103T"), new Email("royb@u.nus.edu"),
                new StudentId("A4444444X"), new TGroup("T12"), new Tele("@royb"),
                    new WeekList(), Progress.NEEDS_ATTENTION)
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
