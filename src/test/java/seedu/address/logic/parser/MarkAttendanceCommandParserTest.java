package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Week;

/**
 * Unit tests for {@link UpdateAttendanceCommandParser}.
 * Pure unit testing: only parses, no model access.
 */
public class MarkAttendanceCommandParserTest {

    private UpdateAttendanceCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new UpdateAttendanceCommandParser();
    }

    @Test
    public void parseValidAttendanceInputReturnsSuccessModel() throws Exception {
        MarkAttendanceCommand command = parser.parse("1 week/1 sta/Y");

        assertEquals(Index.fromOneBased(1), command.index);
        assertEquals(Index.fromOneBased(1), command.weekNumber);
        assertEquals(Week.Status.Y, command.status);
    }

    @Test
    public void parse_validInput_absentSuccess() throws Exception {
        MarkAttendanceCommand command = parser.parse("2 week/3 sta/A");

        assertEquals(Index.fromOneBased(2), command.index);
        assertEquals(Index.fromOneBased(3), command.weekNumber);
        assertEquals(Week.Status.A, command.status);
    }

    @Test
    public void parse_validInput_defaultSuccess() throws Exception {
        MarkAttendanceCommand command = parser.parse("5 week/2 sta/N");

        assertEquals(Index.fromOneBased(5), command.index);
        assertEquals(Index.fromOneBased(2), command.weekNumber);
        assertEquals(Week.Status.N, command.status);
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1 week/1 sta/X")
        );
    }

    @Test
    public void parse_duplicateStatusPrefix_throwsParseException() {
        // Simulate duplicated prefix
        assertThrows(ParseException.class, () ->
                parser.parse("1 week/1 sta/Y sta/A")
        );
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("abc week/1 sta/Y")
        );
    }
}
