package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
 * Contains unit tests for ParserUtil.
 */
public class ParserUtilTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_STUDENT_ID = "123";
    private static final String INVALID_COURSE = "CS 2103";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TG = "T 1";
    private static final String INVALID_TELE = "#handle";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_STUDENT_ID = "A1234567X";
    private static final String VALID_COURSE = "CS2103T";
    private static final String VALID_EMAIL = "e1234567@u.nus.edu";
    private static final String VALID_TG = "T01";
    private static final String VALID_TELE = "@johndoe";

    private static final String MESSAGE_USAGE = "Usage: view INDEX";

    @Test
    public void parseIndex_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseIndex(null));
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1L)));
    }

    @Test
    public void parseIndex_validInput_returnsIndex() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));
        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseIndex_withUsageEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_MISSING_INDEX + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("  ", MESSAGE_USAGE));
    }

    @Test
    public void parseIndex_withUsageTooManyArgs_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_TOO_MANY_ARGUMENTS + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("1 2", MESSAGE_USAGE));
    }

    @Test
    public void parseIndex_withUsageValidArgs_returnsIndex() throws Exception {
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex(" 1 ", MESSAGE_USAGE));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName(null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = "  " + VALID_NAME + "  ";
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseStudentId_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseStudentId(INVALID_STUDENT_ID));
    }

    @Test
    public void parseStudentId_validValue_returnsStudentId() throws Exception {
        assertEquals(new StudentId(VALID_STUDENT_ID), ParserUtil.parseStudentId(VALID_STUDENT_ID));
    }

    @Test
    public void parseCourseId_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourseId(INVALID_COURSE));
    }

    @Test
    public void parseCourseId_validValue_returnsCourseId() throws Exception {
        assertEquals(new CourseId(VALID_COURSE), ParserUtil.parseCourseId(VALID_COURSE));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValue_returnsEmail() throws Exception {
        assertEquals(new Email(VALID_EMAIL), ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseTGroup_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTGroup(INVALID_TG));
    }

    @Test
    public void parseTGroup_validValue_returnsTGroup() throws Exception {
        assertEquals(new TGroup(VALID_TG), ParserUtil.parseTGroup(VALID_TG));
    }

    @Test
    public void parseTele_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTele(INVALID_TELE));
    }

    @Test
    public void parseTele_validValue_returnsTele() throws Exception {
        assertEquals(new Tele(VALID_TELE), ParserUtil.parseTele(VALID_TELE));
    }

    @Test
    public void parseAttendanceStatus_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, "Status must be Y, A, or N", ()
                -> ParserUtil.parseAttendanceStatus("X"));
    }

    @Test
    public void parseAttendanceStatus_validValue_returnsStatus() throws Exception {
        assertEquals(Week.Status.Y, ParserUtil.parseAttendanceStatus("  y  "));
        assertEquals(Week.Status.A, ParserUtil.parseAttendanceStatus("A"));
    }

    @Test
    public void parseWeekIndex_outOfRange_throwsParseException() {
        assertThrows(ParseException.class, WeekList.MESSAGE_INVALID_WEEK, ()
                -> ParserUtil.parseWeekIndex("14"));
    }

    @Test
    public void parseWeekIndex_validValue_returnsIndex() throws Exception {
        assertEquals(Index.fromOneBased(1), ParserUtil.parseWeekIndex(" 1 "));
        assertEquals(Index.fromOneBased(13), ParserUtil.parseWeekIndex("13"));
    }


    @Test
    public void parseWeekStatus_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseWeekStatus("Z"));
    }

    @Test
    public void parseWeekStatus_validValues_returnsStatus() throws Exception {
        assertEquals(Week.Status.Y, ParserUtil.parseWeekStatus("y"));
        assertEquals(Week.Status.A, ParserUtil.parseWeekStatus("a"));
        assertEquals(Week.Status.N, ParserUtil.parseWeekStatus("N"));
    }

    @Test
    public void parseProgress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseProgress("unknown"));
    }

    @Test
    public void parseProgress_validValues_returnsProgress() throws Exception {
        assertEquals(Progress.ON_TRACK, ParserUtil.parseProgress("on_track"));
        assertEquals(Progress.NEEDS_ATTENTION, ParserUtil.parseProgress("NEEDS_ATTENTION"));
        assertEquals(Progress.AT_RISK, ParserUtil.parseProgress("at_risk"));
        assertEquals(Progress.NOT_SET, ParserUtil.parseProgress("clear"));
        assertEquals(Progress.NOT_SET, ParserUtil.parseProgress("not_set"));
    }

    @Test
    public void parseAbsenceCount_nonNumeric_throwsParseException() {
        assertThrows(ParseException.class, "Absence count must be a non-negative integer.", ()
                -> ParserUtil.parseAbsenceCount("abc"));
    }

    @Test
    public void parseAbsenceCount_negativeValue_throwsParseException() {
        assertThrows(ParseException.class, "Absence count must be a non-negative integer.", ()
                -> ParserUtil.parseAbsenceCount("-1"));
    }

    @Test
    public void parseAbsenceCount_validValue_returnsInteger() throws Exception {
        assertEquals(0, ParserUtil.parseAbsenceCount("0"));
        assertEquals(5, ParserUtil.parseAbsenceCount("  5  "));
    }

    @Test
    public void parseKeywords_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseKeywords(null));
    }

    @Test
    public void parseKeywords_emptyArgs_throwsParseException() {
        // Test empty string and string with only whitespaces
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords(""));
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords("   "));
    }

    @Test
    public void parseKeywords_invalidCharacters_throwsParseException() {
        // Numbers are not allowed
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice123"));
        // Special characters (symbols) are not allowed
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice@Bob"));
        // Hyphens/Apostrophes (if not allowed by your current regex)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Anne-Marie"));
    }

    @Test
    public void parseKeywords_validArgs_returnsKeywordsList() throws Exception {
        // Single keyword
        List<String> expectedSingle = List.of("Alice");
        assertEquals(expectedSingle, ParserUtil.parseKeywords("Alice"));

        // Multiple keywords with varying whitespace
        List<String> expectedMultiple = Arrays.asList("Alice", "Bob", "Charlie");
        assertEquals(expectedMultiple, ParserUtil.parseKeywords("  Alice   Bob \n Charlie  "));

        // Mixed case (logic should preserve case, as the Predicate usually handles case-insensitivity)
        List<String> expectedMixed = Arrays.asList("aLiCe", "bOB");
        assertEquals(expectedMixed, ParserUtil.parseKeywords("aLiCe bOB"));
    }
}
