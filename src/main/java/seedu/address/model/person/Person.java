package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final CourseId courseId;
    private final Email email;
    private final StudentId studentId;
    private final TGroup tGroup;
    private final Tele tele;
    private final Progress progress;
    private final List<Remark> remarks;

    private final WeekList weekList;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, CourseId courseId, Email email, StudentId studentId,
                  TGroup tGroup, Tele tele, WeekList weekList, Progress progress) {
        requireAllNonNull(name, courseId, email, studentId, tGroup, weekList, progress);
        this.name = name;
        this.courseId = courseId;
        this.email = email;
        this.studentId = studentId;
        this.tGroup = tGroup;
        this.tele = tele;
        this.weekList = weekList;
        this.progress = progress;
        this.remarks = new ArrayList<>();
    }

    public Name getName() {
        return name;
    }

    public CourseId getCourseId() {
        return courseId;
    }

    public Email getEmail() {
        return email;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public TGroup getTGroup() {
        return tGroup;
    }

    public Tele getTele() {
        return tele;
    }

    public WeekList getWeekList() {
        return weekList;
    }
    public Progress getProgress() {
        return progress;
    }

    public int getAbsenceCount() {
        return weekList.calculateWeekAbsence();
    }
    /**
     * Returns an unmodifiable view of the remarks list.
     */
    public List<Remark> getRemarks() {
        return Collections.unmodifiableList(remarks);
    }

    /**
     * Adds a remark to this person.
     */
    public void addRemark(Remark remark) {
        requireAllNonNull(remark);
        remarks.add(remark);
    }

    /**
     * Deletes a remark from this person.
     *
     * @return true if the remark was found and removed
     */
    public boolean deleteRemark(Index remarkIndex) {
        requireAllNonNull(remarkIndex);
        if (remarkIndex.getZeroBased() >= remarks.size()) {
            return false;
        }
        remarks.remove(remarks.get(remarkIndex.getZeroBased()));
        return true;
    }
    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getStudentId().equals(getStudentId());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && courseId.equals(otherPerson.courseId)
                && studentId.equals(otherPerson.studentId)
                && tGroup.equals(otherPerson.tGroup)
                && email.equals(otherPerson.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                name, courseId, email, studentId, tGroup, tele);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("courseId", courseId)
                .add("email", email)
                .add("studentId", studentId)
                .add("tGroup", tGroup)
                .add("tele", tele)
                .add("weekList", weekList)
                .add("progress", progress)
                .toString();
    }

}
