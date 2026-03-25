package seedu.address.testutil;

import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;
import seedu.address.model.person.WeekList;
import seedu.address.model.person.WeeklyAttendanceList;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_EMAIL = "amy@u.nus.edu";
    public static final String DEFAULT_COURSE_ID = "CS2103T";
    public static final String DEFAULT_STUDENT_ID = "A1234567X";
    public static final String DEFAULT_TGROUP = "T01";
    public static final String DEFAULT_TELE = "91234567";

    private Name name;
    private Email email;
    private CourseId courseId;
    private StudentId studentId;
    private TGroup tGroup;
    private Tele tele;
    private WeeklyAttendanceList weeklyAttendanceList;
    private Progress progress;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        email = new Email(DEFAULT_EMAIL);
        courseId = new CourseId(DEFAULT_COURSE_ID);
        studentId = new StudentId(DEFAULT_STUDENT_ID);
        tGroup = new TGroup(DEFAULT_TGROUP);
        tele = new Tele(DEFAULT_TELE);
        weeklyAttendanceList = new WeekList();
        progress = Progress.NOT_SET;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     * @param personToCopy The person object to copy data from.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        email = personToCopy.getEmail();
        courseId = personToCopy.getCourseId();
        studentId = personToCopy.getStudentId();
        tGroup = personToCopy.getTGroup();
        tele = personToCopy.getTele();
        weeklyAttendanceList = personToCopy.getWeeklyAttendanceList();
        progress = personToCopy.getProgress();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code CourseId} of the {@code Person} that we are building.
     */
    public PersonBuilder withCourseId(String courseId) {
        this.courseId = new CourseId(courseId);
        return this;
    }

    /**
     * Sets the {@code StudentId} of the {@code Person} that we are building.
     */
    public PersonBuilder withStudentId(String studentId) {
        this.studentId = new StudentId(studentId);
        return this;
    }

    /**
     * Sets the {@code TGroup} of the {@code Person} that we are building.
     */
    public PersonBuilder withTGroup(String tGroup) {
        this.tGroup = new TGroup(tGroup);
        return this;
    }

    /**
     * Sets the {@code Tele} of the {@code Person} that we are building.
     */
    public PersonBuilder withTele(String tele) {
        this.tele = new Tele(tele);
        return this;
    }

    /**
     * Sets the {@code WeeklyAttendanceList} of the {@code Person} that we are building.
     */
    public PersonBuilder withWeeklyAttendanceList(WeeklyAttendanceList list) {
        this.weeklyAttendanceList = list;
        return this;
    }

    /**
     * Sets the {@code Progress} of the {@code Person} that we are building.
     */
    public PersonBuilder withProgress(Progress progress) {
        this.progress = progress;
        return this;
    }

    /**
     * Sets the {@code WeeklyAttendanceList} to have a specific number of absences.
     * This is useful for testing filtering by absence count.
     * @param count The number of weeks to mark as absent (starting from Week 1).
     */
    public PersonBuilder withAbsences(int count) {
        this.weeklyAttendanceList = new WeekList();
        for (int i = 0; i < count; i++) {
            this.weeklyAttendanceList.markWeekAsAbsent(i);
        }
        return this;
    }

    /**
     * Builds and returns a {@code Person} with the specified details.
     */
    public Person build() {
        return new Person(name, courseId, email, studentId, tGroup, tele, weeklyAttendanceList, progress);
    }
}
