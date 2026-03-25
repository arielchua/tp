package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STUDENT_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TELE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TGROUP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_COURSE_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COURSE_ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.COURSE_ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.STUDENT_ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.STUDENT_ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TGROUP_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TGROUP_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TELE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TELE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COURSE_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TGROUP_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // All compulsory fields + optional tele present
        Person expectedPerson = new PersonBuilder(BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB
                        + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB + TELE_DESC_BOB,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // tele is optional — omitting it should still succeed
        Person expectedPerson = new PersonBuilder(AMY).withTele(null).build();
        assertParseSuccess(parser,
                NAME_DESC_AMY + STUDENT_ID_DESC_AMY + EMAIL_DESC_AMY
                        + COURSE_ID_DESC_AMY + TGROUP_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_repeatedNonTeleValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB
                + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB + TELE_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple student IDs
        assertParseFailure(parser, STUDENT_ID_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STUDENTID));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple course IDs
        assertParseFailure(parser, COURSE_ID_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COURSEID));

        // multiple tgroups
        assertParseFailure(parser, TGROUP_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TGROUP));

        // multiple tele
        assertParseFailure(parser, TELE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TELE));

        // multiple fields repeated simultaneously
        assertParseFailure(parser,
                validExpectedPersonString + NAME_DESC_AMY + EMAIL_DESC_AMY
                        + STUDENT_ID_DESC_AMY + COURSE_ID_DESC_AMY + TGROUP_DESC_AMY + TELE_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_NAME, PREFIX_EMAIL, PREFIX_STUDENTID,
                        PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE));

        // invalid value followed by valid value — still counts as duplicate prefix

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid student ID
        assertParseFailure(parser, INVALID_STUDENT_ID_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STUDENTID));

        // invalid course ID
        assertParseFailure(parser, INVALID_COURSE_ID_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COURSEID));

        // invalid tgroup
        assertParseFailure(parser, INVALID_TGROUP_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TGROUP));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid student ID
        assertParseFailure(parser, validExpectedPersonString + INVALID_STUDENT_ID_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STUDENTID));

        // invalid course ID
        assertParseFailure(parser, validExpectedPersonString + INVALID_COURSE_ID_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COURSEID));

        // invalid tgroup
        assertParseFailure(parser, validExpectedPersonString + INVALID_TGROUP_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TGROUP));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                expectedMessage);

        // missing student ID prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + VALID_STUDENT_ID_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + VALID_EMAIL_BOB + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                expectedMessage);

        // missing course ID prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + VALID_COURSE_ID_BOB + TGROUP_DESC_BOB,
                expectedMessage);

        // missing tgroup prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + VALID_TGROUP_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                VALID_NAME_BOB + VALID_STUDENT_ID_BOB + VALID_EMAIL_BOB + VALID_COURSE_ID_BOB + VALID_TGROUP_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid student ID
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_STUDENT_ID_DESC + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                StudentId.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + INVALID_EMAIL_DESC + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid course ID
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + INVALID_COURSE_ID_DESC + TGROUP_DESC_BOB,
                CourseId.MESSAGE_CONSTRAINTS);

        // invalid tgroup
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB + INVALID_TGROUP_DESC,
                TGroup.MESSAGE_CONSTRAINTS);

        // invalid tele (optional field, but still validated when present)
        assertParseFailure(parser,
                NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB + COURSE_ID_DESC_BOB
                        + TGROUP_DESC_BOB + INVALID_TELE_DESC,
                Tele.MESSAGE_CONSTRAINTS);

        // two invalid values — only the first invalid value is reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB
                        + INVALID_COURSE_ID_DESC + TGROUP_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + STUDENT_ID_DESC_BOB + EMAIL_DESC_BOB
                        + COURSE_ID_DESC_BOB + TGROUP_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
