package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Extensive orchestration tests for FilterCommandParser.
 * Structural validations (preambles, bare prefixes) are tested in ParserValidatorsTest.
 */
public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_noFiltersProvided_failure() {
        // Ensures the command cannot be run without at least one criteria
        assertParseFailure(parser, "", FilterCommandParser.MESSAGE_NO_FILTERS);
        assertParseFailure(parser, "    ", FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // Verification of the "Multiple values specified" error handling
        assertParseFailure(parser, " crs/CS2103T crs/CS2101",
                String.format(MESSAGE_DUPLICATE_FIELDS, PREFIX_COURSEID));
    }

    @Test
    public void parse_variousFieldCombinations_success() {
        // Case 1: CourseId and Progress only
        FilterMatchesPredicate pred1 = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(),
                Optional.of(Progress.ON_TRACK), Optional.empty());
        assertParseSuccess(parser, " crs/CS2103T p/on_track", new FilterCommand(pred1));

        // Case 2: TGroup and Absence Count only
        FilterMatchesPredicate pred2 = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T05")),
                Optional.empty(), Optional.of(10));
        assertParseSuccess(parser, " tg/T05 abs/10", new FilterCommand(pred2));

        // Case 3: CourseId, TGroup, and Absence (Skipping Progress field)
        FilterMatchesPredicate pred3 = new FilterMatchesPredicate(
                Optional.of(new CourseId("MA1521")), Optional.of(new TGroup("T01")),
                Optional.empty(), Optional.of(0));
        assertParseSuccess(parser, " crs/MA1521 tg/T01 abs/0", new FilterCommand(pred3));
    }

    @Test
    public void parse_shuffledOrder_success() {
        // Ensures that the order of flags in the user input does not affect the Predicate
        FilterMatchesPredicate expectedPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.AT_RISK),
                Optional.of(5));

        // Shuffled version: abs -> p -> tg -> crs
        assertParseSuccess(parser, " abs/5 p/at_risk tg/T01 crs/CS2103T", new FilterCommand(expectedPredicate));

        // Shuffled version: tg -> crs -> abs -> p
        assertParseSuccess(parser, " tg/T01 crs/CS2103T abs/5 p/at_risk", new FilterCommand(expectedPredicate));
    }

    @Test
    public void parse_extraWhitespaces_success() {
        // Robustness test for messy user input
        FilterMatchesPredicate expectedPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("MA1521")), Optional.empty(), Optional.empty(), Optional.of(0));

        String input = "    crs/MA1521   \n    abs/0  ";
        assertParseSuccess(parser, input, new FilterCommand(expectedPredicate));
    }
}
