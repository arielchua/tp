package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
                        PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE);

        Prefix[] allowedPrefixes = {
            PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
            PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE
        };
        String allowedReadable = "n/, sid/, e/, crs/, tg/, tele/";

        ParserValidators.checkForUnknownPrefixTokens(args, allowedPrefixes,
                allowedReadable, AddCommand.MESSAGE_USAGE);
        ParserValidators.checkForBarePrefixes(argMultimap, allowedPrefixes,
                AddCommand.MESSAGE_USAGE);
        ParserValidators.checkForMissingValues(argMultimap,
                new Prefix[]{
                    PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL, PREFIX_COURSEID, PREFIX_TGROUP},
                new String[]{
                    "n/", "sid/", "e/", "crs/", "tg/"},
                new String[]{
                    "Name cannot be empty.", "Student ID cannot be empty.",
                    "Email cannot be empty.", "Course ID cannot be empty.",
                    "Tutorial group cannot be empty."},
                AddCommand.MESSAGE_USAGE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(pe.getMessage()));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
            PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENTID).isPresent()) {
            editPersonDescriptor.setStudentId(ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_COURSEID).isPresent()) {
            editPersonDescriptor.setCourseId(ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get()));
        }
        if (argMultimap.getValue(PREFIX_TGROUP).isPresent()) {
            editPersonDescriptor.setTGroup(ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get()));
        }
        if (argMultimap.getValue(PREFIX_TELE).isPresent()) {
            editPersonDescriptor.setTele(ParserUtil.parseTele(argMultimap.getValue(PREFIX_TELE).get()));
        }

        ParserValidators.checkForUnknownPrefixTokens(args, allowedPrefixes,
                allowedReadable, AddCommand.MESSAGE_USAGE);
        ParserValidators.checkForBarePrefixes(argMultimap, allowedPrefixes,
                AddCommand.MESSAGE_USAGE);
        ParserValidators.checkForMissingValues(argMultimap,
                new Prefix[] {
                    PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL, PREFIX_COURSEID, PREFIX_TGROUP },
                new String[] {
                    "n/", "sid/", "e/", "crs/", "tg/" },
                new String[] {
                    "Name cannot be empty.", "Student ID cannot be empty.",
                    "Email cannot be empty.", "Course ID cannot be empty.",
                    "Tutorial group cannot be empty." },
                AddCommand.MESSAGE_USAGE);

        return new EditCommand(index, editPersonDescriptor);
    }

}
