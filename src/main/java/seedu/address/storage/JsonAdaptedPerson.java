package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String courseId;
    private final String email;
    private final String studentId;
    private final String tGroup;
    private final String tele;
    private final String weeklyAttendanceList;
    private final String progress;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
            @JsonProperty("courseId") String courseId,
            @JsonProperty("email") String email,
            @JsonProperty("studentId") String studentId,
            @JsonProperty("tGroup") String tGroup,
            @JsonProperty("tele") String tele,
            @JsonProperty("weeklyAttendanceList") String weeklyAttendanceList,
            @JsonProperty("progress") String progress) {
        this.name = name;
        this.courseId = courseId;
        this.email = email;
        this.studentId = studentId;
        this.tGroup = tGroup;
        this.tele = tele;
        this.weeklyAttendanceList = weeklyAttendanceList;
        this.progress = progress;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        courseId = source.getCourseId().value;
        email = source.getEmail().value;
        studentId = source.getStudentId().value;
        tGroup = source.getTGroup().value;
        tele = source.getTele() == null ? null : source.getTele().value;
        weeklyAttendanceList = source.getWeeklyAttendanceList().toString();
        progress = source.getProgress().name();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (courseId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CourseId.class.getSimpleName()));
        }
        if (!CourseId.isValidCourseId(courseId)) {
            throw new IllegalValueException(CourseId.MESSAGE_CONSTRAINTS);
        }
        final CourseId modelCourseId = new CourseId(courseId);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (studentId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StudentId.class.getSimpleName()));
        }
        if (!StudentId.isValidStudentId(studentId)) {
            throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
        }
        final StudentId modelStudentId = new StudentId(studentId);

        if (tGroup == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    TGroup.class.getSimpleName()));
        }
        if (!TGroup.isValidTGroup(tGroup)) {
            throw new IllegalValueException(TGroup.MESSAGE_CONSTRAINTS);
        }
        final TGroup modelTGroup = new TGroup(tGroup);

        Tele modelTele = null;
        if (tele != null) {
            if (!Tele.isValidTele(tele)) {
                throw new IllegalValueException(Tele.MESSAGE_CONSTRAINTS);
            }
            modelTele = new Tele(tele);
        }

        if (weeklyAttendanceList == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    WeeklyAttendanceList.class.getSimpleName()));
        }
        WeeklyAttendanceList modelWeeklyAttendanceList;
        try {
            modelWeeklyAttendanceList = WeekList.buildWeekListFromString(weeklyAttendanceList);
        } catch (IllegalValueException e) {
            throw new IllegalValueException("Invalid weekly attendance data: " + e.getMessage());
        }

        Progress modelProgress = Progress.NOT_SET;
        if (progress != null) {
            try {
                modelProgress = Progress.valueOf(progress);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException("Invalid progress value: " + progress);
            }
        }

        return new Person(modelName, modelCourseId, modelEmail,
                modelStudentId, modelTGroup, modelTele, modelWeeklyAttendanceList, modelProgress);
    }

}
