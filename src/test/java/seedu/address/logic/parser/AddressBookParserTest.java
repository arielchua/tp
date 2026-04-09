package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CancelWeekCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.commands.ProgressCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.UnCancelWeekCommand;
import seedu.address.logic.commands.UnremarkCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

/**
 * Contains unit tests for AddressBookParser.
 */
public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("Alice", "Bob", "Charlie");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        HashMap<String, String> args = new HashMap<>();
        args.put("course", "CS2103T");
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                java.util.Optional.of(new seedu.address.model.person.CourseId("CS2103T")),
                java.util.Optional.empty(),
                java.util.Optional.empty(),
                java.util.Optional.empty()
        );
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " crs/CS2103T");
        assertEquals(new FilterCommand(predicate), command);
    }

    @Test
    public void parseCommand_remark() throws Exception {
        final String text = "Good progress";
        RemarkCommand command = (RemarkCommand) parser.parseCommand(
                RemarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_REMARK + text);
        assertEquals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark(text, LocalDate.now())), command);
    }

    @Test
    public void parseCommand_unremark() throws Exception {
        UnremarkCommand command = (UnremarkCommand) parser.parseCommand(
                UnremarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_UNREMARK + "1");
        assertEquals(new UnremarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_cancelWeek() throws Exception {
        CancelWeekCommand command = (CancelWeekCommand) parser.parseCommand(
                CancelWeekCommand.COMMAND_WORD + " crs/CS2103T tg/T01 week/1");
        assertEquals(new CancelWeekCommand(new seedu.address.model.person.CourseId("CS2103T"),
                new seedu.address.model.person.TGroup("T01"),
                seedu.address.commons.core.index.Index.fromOneBased(1)), command);
    }

    @Test
    public void parseCommand_unCancelWeek() throws Exception {
        UnCancelWeekCommand command = (UnCancelWeekCommand) parser.parseCommand(
                UnCancelWeekCommand.COMMAND_WORD + " crs/CS2103T tg/T01 week/1");
        assertEquals(new UnCancelWeekCommand(new seedu.address.model.person.CourseId("CS2103T"),
                new seedu.address.model.person.TGroup("T01"),
                seedu.address.commons.core.index.Index.fromOneBased(1)), command);
    }

    @Test
    public void parseCommand_markAttendance() throws Exception {
        MarkAttendanceCommand command = (MarkAttendanceCommand) parser.parseCommand(
                MarkAttendanceCommand.COMMAND_WORD + " 1 week/1 sta/Y");
        assertEquals(new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                seedu.address.commons.core.index.Index.fromOneBased(1),
                seedu.address.model.person.Week.Status.Y), command);
    }

    @Test
    public void parseCommand_progress() throws Exception {
        ProgressCommand command = (ProgressCommand) parser.parseCommand(
                ProgressCommand.COMMAND_WORD + " 1 p/on_track");
        assertEquals(new ProgressCommand(INDEX_FIRST_PERSON,
                seedu.address.model.person.Progress.ON_TRACK), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), (
                ) -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, (
        ) -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_withExtraWhitespace_success() throws Exception {
        // Extra whitespace before command
        assertTrue(parser.parseCommand("  " + ListCommand.COMMAND_WORD) instanceof ListCommand);

        // Extra whitespace after command
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + "  ") instanceof ListCommand);

        // Extra whitespace between command and arguments
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + "  " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }
}
