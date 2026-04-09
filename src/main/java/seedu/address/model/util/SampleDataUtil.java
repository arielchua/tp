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
            createWeekList(
                new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
                new int[] {},
                new int[] {}
            ),
            Progress.ON_TRACK);
        alex.addRemark(new Remark("Participates actively in class", LocalDate.of(2026, 2, 3)));
        alex.addRemark(new Remark("Usually well-prepared for tutorials", LocalDate.of(2026, 3, 10)));

        Person bernice = new Person(
            new Name("Bernice Yu"),
            new CourseId("CS2103T"),
            new Email("berniceyu@u.nus.edu"),
            new StudentId("A7654321X"),
            new TGroup("T12"),
            new Tele("@berniceyu"),
            createWeekList(
                new int[] {0, 1, 2, 4, 5, 7, 8, 9, 10},
                new int[] {3, 6, 11},
                new int[] {}
            ),
            Progress.NEEDS_ATTENTION);
            bernice.addRemark(new Remark("Attendance is inconsistent in later weeks", LocalDate.of(2026, 3, 24)));

        Person charlotte = new Person(
            new Name("Charlotte Oliveiro"),
            new CourseId("CS2030S"),
            new Email("charlotte@u.nus.edu"),
            new StudentId("A1111111X"),
            new TGroup("T08"),
            new Tele("@charlotte"),
            createWeekList(
                new int[] {},
                new int[] {0, 1, 2, 3, 4, 5},
                new int[] {6}
            ),
            Progress.NOT_SET);
        charlotte.addRemark(new Remark("Has missed multiple tutorial sessions", LocalDate.of(2026, 3, 1)));

        Person david = new Person(
            new Name("David Li"),
            new CourseId("CS2030S"),
            new Email("lidavid@u.nus.edu"),
            new StudentId("A2222222X"),
            new TGroup("T08"),
            new Tele("@lidavid"),
            createWeekList(
                new int[] {0, 1, 2, 3, 5, 6, 7, 9, 10, 11},
                new int[] {4, 8},
                new int[] {}
            ),
            Progress.NOT_SET);

        Person irfan = new Person(
            new Name("Irfan Ibrahim"),
            new CourseId("CS2040S"),
            new Email("irfan@u.nus.edu"),
            new StudentId("A3333333X"),
            new TGroup("T05"),
            new Tele("@irfan"),
            createWeekList(
                new int[] {0, 2, 3, 5, 6},
                new int[] {1, 4, 7, 8},
                new int[] {9}
            ),
            Progress.AT_RISK);
        irfan.addRemark(new Remark("Needs to improve participation", LocalDate.of(2026, 2, 24)));
        irfan.addRemark(new Remark("Seems to be struggling with weekly material", LocalDate.of(2026, 3, 1)));

        Person roy = new Person(
            new Name("Roy Balakrishnan"),
            new CourseId("CS2040S"),
            new Email("royb@u.nus.edu"),
            new StudentId("A4444444X"),
            new TGroup("T05"),
            new Tele("@royb"),
            createWeekList(
                new int[] {0, 1, 3, 4, 5, 6, 8, 9, 11},
                new int[] {2, 7, 10},
                new int[] {}
            ),
            Progress.NEEDS_ATTENTION);

        return new Person[] {
            alex, bernice, charlotte, david, irfan, roy
        };
    }

    /**
     * Creates a WeekList with the given attended, absent, and cancelled week indices.
     * Indices are 0-based:
     * week 1 = 0, week 2 = 1, ..., week 13 = 12.
     */
    private static WeekList createWeekList(int[] attendedWeeks, int[] absentWeeks, int[] cancelledWeeks) {
        WeekList weekList = new WeekList();

        for (int weekIndex : attendedWeeks) {
            weekList.markWeekAsAttended(weekIndex);
        }

        for (int weekIndex : absentWeeks) {
            weekList.markWeekAsAbsent(weekIndex);
        }

        for (int weekIndex : cancelledWeeks) {
            weekList.markAsCancelled(weekIndex);
        }

        return weekList;
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }
}
