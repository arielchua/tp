package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

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
import seedu.address.model.person.Week;
import seedu.address.model.person.WeekList;


/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String KEYWORDS_VALIDATION_REGEX = "[A-Za-z]+(\\s+[A-Za-z]+)*";
    public static final String MESSAGE_INVALID_INDEX = "Index must be a non-zero unsigned integer.";
    public static final String MESSAGE_MISSING_INDEX = "Missing student index.";
    public static final String MESSAGE_TOO_MANY_ARGUMENTS = "Only one student index is allowed.";
    public static final String MESSAGE_INVALID_PROGRESS =
            "Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear.";
    public static final String MESSAGE_INVALID_ABSENCE_COUNT =
            "Absence count must be a non-negative integer.";
    public static final String MESSAGE_INVALID_KEYWORDS =
            "Keywords should contain alphabetic characters separated by spaces only.";
    public static final String MESSAGE_EMPTY_KEYWORDS =
            "Find command requires at least one keyword.";
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        requireNonNull(oneBasedIndex);
        String trimmedIndex = oneBasedIndex.trim();

        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        try {
            return Index.fromOneBased(Integer.parseInt(trimmedIndex));
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_INDEX, e);
        }
    }

    /**
     * Parses arguments containing exactly one index and returns the parsed {@code Index}.
     *
     * @param args Raw argument string.
     * @param messageUsage Usage message of the calling command.
     * @return Parsed index.
     * @throws ParseException if the index is missing, duplicated, or invalid.
     */
    public static Index parseIndex(String args, String messageUsage) throws ParseException {
        requireNonNull(args);
        requireNonNull(messageUsage);

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_INDEX + "\n" + messageUsage);
        }

        String[] tokens = trimmedArgs.split("\\s+");
        if (tokens.length > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_ARGUMENTS + "\n" + messageUsage);
        }

        try {
            return parseIndex(tokens[0]);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage() + "\n" + messageUsage);
        }
    }

    /**
     * Parses {@code keywords} into a list of strings.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the keywords are empty or invalid.
     */
    public static List<String> parseKeywords(String keywords) throws ParseException {
        requireNonNull(keywords);
        String trimmed = keywords.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_KEYWORDS);
        }

        if (!trimmed.matches(KEYWORDS_VALIDATION_REGEX)) {
            throw new ParseException(MESSAGE_INVALID_KEYWORDS);
        }

        return Arrays.asList(trimmed.split("\\s+"));
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
            throw new ParseException(CourseId.MESSAGE_CONSTRAINTS);
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
            throw new ParseException(TGroup.MESSAGE_CONSTRAINTS);
        }
        return new TGroup(trimmedTGroup);
    }

    /**
     * Parses {@code tele} into a {@code Tele}.
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
     * Parses a {@code String} into a {@code Week.Status}.
     *
     * @throws ParseException if the input is invalid
     */
    public static Week.Status parseAttendanceStatus(String status) throws ParseException {
        requireNonNull(status);
        String trimmed = status.trim().toUpperCase();

        try {
            return Week.Status.fromString(trimmed);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Status must be Y, A, or N");
        }
    }

    /**
     * Parses a {@code String} into an {@code Index} representing a week number.
     *
     * @param oneBasedWeek The input string, e.g., "1" for week 1.
     * @return Index corresponding to the week number.
     * @throws ParseException if the input is not a positive integer.
     */
    public static Index parseWeekIndex(String oneBasedWeek) throws ParseException {
        Index index = parseIndex(oneBasedWeek);

        if (index.getOneBased() > 13) {
            throw new ParseException(WeekList.MESSAGE_INVALID_WEEK);
        }

        return index;
    }

    /**
     * Parses a {@code String} into a {@code Week.Status}.
     * Accepts "y" = attended, "a" = absent, "n" = not marked.
     *
     * @param input The input string representing attendance status.
     * @return Week.Status corresponding to the input.
     * @throws ParseException if the input is invalid.
     */
    public static Week.Status parseWeekStatus(String input) throws ParseException {
        requireNonNull(input);
        String trimmed = input.trim().toUpperCase();

        switch (trimmed) {
        case "Y":
            return Week.Status.Y;
        case "A":
            return Week.Status.A;
        case "N":
            return Week.Status.N;
        default:
            throw new ParseException(Week.MESSAGE_CONSTRAINTS);
        }
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
            throw new ParseException(MESSAGE_INVALID_PROGRESS);
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
            throw new ParseException(MESSAGE_INVALID_ABSENCE_COUNT);
        }

        return Integer.parseInt(trimmed);
    }
}
