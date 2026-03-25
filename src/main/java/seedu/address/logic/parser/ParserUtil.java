package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;


/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String studentId} into a {@code StudentId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code studentId} is invalid.
     */
    public static StudentId parseStudentId(String studentId) throws ParseException {
        requireNonNull(studentId);
        String trimmedStudentId = studentId.trim();
        if (!StudentId.isValidStudentId(trimmedStudentId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }
        return new StudentId(trimmedStudentId);
    }

    /**
     * Parses a {@code String courseId} into an {@code CourseId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code courseId} is invalid.
     */
    public static CourseId parseCourseId(String courseId) throws ParseException {
        requireNonNull(courseId);
        String trimmedCourseId = courseId.trim();
        if (!CourseId.isValidCourseId(trimmedCourseId)) {
            throw new ParseException("Invalid course ID: " + trimmedCourseId + "\n" + CourseId.MESSAGE_CONSTRAINTS);
        }
        return new CourseId(trimmedCourseId);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String TGroup} into a {@code TGroup}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code TGroup} is invalid.
     */
    public static TGroup parseTGroup(String tGroup) throws ParseException {
        requireNonNull(tGroup);
        String trimmedTGroup = tGroup.trim();
        if (!TGroup.isValidTGroup(trimmedTGroup)) {
            throw new ParseException("Invalid tutorial ID: " + trimmedTGroup + "\n" + TGroup.MESSAGE_CONSTRAINTS);
        }
        return new TGroup(trimmedTGroup);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Tele parseTele(String tele) throws ParseException {
        requireNonNull(tele);
        String trimmedTele = tele.trim();
        if (!Tele.isValidTele(trimmedTele)) {
            throw new ParseException(Tele.MESSAGE_CONSTRAINTS);
        }
        return new Tele(trimmedTele);
    }

    /**
     * Parses a {@code String progress} into a {@code Progress}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code progress} is invalid.
     */
    public static Progress parseProgress(String progress) throws ParseException {
        requireNonNull(progress);
        String normalizedProgress = progress.trim().toLowerCase();

        switch (normalizedProgress) {
        case "on_track":
            return Progress.ON_TRACK;
        case "needs_attention":
            return Progress.NEEDS_ATTENTION;
        case "at_risk":
            return Progress.AT_RISK;
        case "clear":
        case "not_set":
            return Progress.NOT_SET;
        default:
            throw new ParseException(
                    "Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear.");
        }
    }
    /**
     * Parses {@code stringAbsenceCount} into an {@code Integer} absence count threshold.
     *
     * @throws ParseException if the given string is not a non-negative integer
     */
    public static Integer parseAbsenceCount(String stringAbsenceCount) throws ParseException {
        requireNonNull(stringAbsenceCount);
        String trimmed = stringAbsenceCount.trim();

        if (!trimmed.matches("\\d+")) {
            throw new ParseException("Absence count must be a non-negative integer.");
        }

        return Integer.parseInt(trimmed);
    }
}
