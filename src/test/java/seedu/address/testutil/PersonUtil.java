package seedu.address.testutil;

// import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;


/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME).append(person.getName().fullName).append(" ");
        sb.append(PREFIX_STUDENTID).append(person.getStudentId().value).append(" ");
        sb.append(PREFIX_EMAIL).append(person.getEmail().value).append(" ");
        sb.append(PREFIX_COURSEID).append(person.getCourseId().value).append(" ");
        sb.append(PREFIX_TGROUP).append(person.getTGroup().value).append(" ");
        if (person.getTele() != null) {
            sb.append(PREFIX_TELE).append(person.getTele().value).append(" ");
        }
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getStudentId().ifPresent(id -> sb.append(PREFIX_STUDENTID).append(id.value).append(" "));
        descriptor.getCourseId().ifPresent(crs -> sb.append(PREFIX_COURSEID).append(crs.value).append(" "));
        descriptor.getTGroup().ifPresent(tg -> sb.append(PREFIX_TGROUP).append(tg.value).append(" "));
        descriptor.getTele().ifPresent(tele -> sb.append(PREFIX_TELE).append(tele.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        return sb.toString();
    }
}

