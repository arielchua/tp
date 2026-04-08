package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Progress;
import seedu.address.model.person.WeekList;

/**
 * Contains unit tests for ParserUtil.
 */
public class ParserUtilTest {

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_STUDENT_ID = "A1234567X";
    private static final String VALID_COURSE = "CS2103T";
    private static final String VALID_TG = "T01";

    private static final String MESSAGE_USAGE = "Usage: view INDEX";

    // ----------------------- INDEX PARSING -----------------------

    @Test
    public void parseIndex_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseIndex(null));
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        // EP: Non-numeric strings
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_zeroInput_throwsParseException() {
        // BVA: Zero (Classic off-by-one boundary for 1-based indexing)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex("0"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        // BVA: Integer overflow
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1L)));
    }

    @Test
    public void parseIndex_validInput_returnsIndex() throws Exception {
        // EP: Smallest valid value (1)
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));
        // EP: Value with whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseIndex_withUsageEmptyArgs_throwsParseException() {
        // EP: Empty/Whitespace preamble
        assertThrows(ParseException.class, ParserUtil.MESSAGE_MISSING_INDEX + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("  ", MESSAGE_USAGE));
    }

    @Test
    public void parseIndex_withUsageTooManyArgs_throwsParseException() {
        // EP: Multiple tokens when only one is expected (Safety check for multi-delete prevention)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_TOO_MANY_ARGUMENTS + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("1 2", MESSAGE_USAGE));
    }

    // ----------------------- KEYWORD PARSING -----------------------

    @Test
    public void parseKeywords_emptyArgs_throwsParseException() {
        // EP: Empty string and whitespace-only strings
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords(""));
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords("   "));
    }

    @Test
    public void parseKeywords_invalidCharacters_throwsParseException() {
        // EP: Numbers/Symbols (Violates KEYWORDS_VALIDATION_REGEX)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice123"));
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice@Bob"));
    }

    @Test
    public void parseKeywords_validArgs_returnsKeywordsList() throws Exception {
        // EP: Multiple keywords + varying whitespace + newlines
        List<String> expectedMultiple = Arrays.asList("Alice", "Bob", "Charlie");
        assertEquals(expectedMultiple, ParserUtil.parseKeywords("  Alice   Bob \n Charlie  "));
    }

    // ----------------------- PROGRESS & ATTENDANCE -----------------------

    @Test
    public void parseProgress_validValues_returnsProgress() throws Exception {
        // Normalization Test: Case insensitivity + Mapping 'clear' to NOT_SET
        assertEquals(Progress.ON_TRACK, ParserUtil.parseProgress("on_track"));
        assertEquals(Progress.NEEDS_ATTENTION, ParserUtil.parseProgress("NEEDS_ATTENTION"));
        assertEquals(Progress.NOT_SET, ParserUtil.parseProgress("clear"));
    }

    @Test
    public void parseWeekIndex_boundaries_throwsParseException() {
        // BVA: Zero (Lower bound)
        assertThrows(ParseException.class, WeekList.MESSAGE_INVALID_WEEK, ()
                -> ParserUtil.parseWeekIndex("0"));

        // BVA: Week 14 (Upper bound limit in NUS Semester)
        assertThrows(ParseException.class, WeekList.MESSAGE_INVALID_WEEK, ()
                -> ParserUtil.parseWeekIndex("14"));
    }

    @Test
    public void parseWeekIndex_validValues_returnsIndex() throws Exception {
        // BVA: Week 1
        assertEquals(1, ParserUtil.parseWeekIndex("1").getOneBased());
        // BVA: Week 13
        assertEquals(13, ParserUtil.parseWeekIndex("13").getOneBased());
    }

    // ----------------------- ABSENCE COUNT -----------------------

    @Test
    public void parseAbsenceCount_invalidValue_throwsParseException() {
        // BVA: Negative value
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_ABSENCE_COUNT, ()
                -> ParserUtil.parseAbsenceCount("-1"));

        // EP: Non-numeric
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_ABSENCE_COUNT, ()
                -> ParserUtil.parseAbsenceCount("five"));
    }

    @Test
    public void parseAbsenceCount_validValue_returnsInteger() throws Exception {
        // BVA: Zero (Minimum non-negative)
        assertEquals(0, (int) ParserUtil.parseAbsenceCount("0"));
        // EP: Positive integer
        assertEquals(10, (int) ParserUtil.parseAbsenceCount("  10  "));
    }
}
