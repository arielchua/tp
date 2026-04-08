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
 * Focuses on EP and BVA for command-line argument combinations and structural constraints.
 */
public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_missingArguments_failure() {
        // EP: Empty or blank inputs (BVA: 0 filters provided)
        assertParseFailure(parser, "", FilterCommandParser.MESSAGE_NO_FILTERS);
        assertParseFailure(parser, "    ", FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void parse_invalidStructure_failure() {
        // EP: Unexpected preamble before prefixes
        assertParseFailure(parser, " 123 crs/CS2101",
                ParserMessages.MESSAGE_UNEXPECTED_PREAMBLE + "\n" + FilterCommand.MESSAGE_USAGE);

        // EP: Duplicate prefixes (Constraint: Only one value per prefix)
        assertParseFailure(parser, " crs/CS2103T crs/CS2101",
                String.format(MESSAGE_DUPLICATE_FIELDS, PREFIX_COURSEID));
    }

    @Test
    public void parse_validSingleFilter_success() {
        // EP: Single filter provided (BVA: Minimum valid filters)
        FilterMatchesPredicate expectedPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(),
                Optional.empty(), Optional.empty());
        assertParseSuccess(parser, " crs/CS2103T", new FilterCommand(expectedPredicate));
    }

    @Test
    public void parse_multipleFilters_success() {
        // EP: Various field combinations (Integration check for multi-prefix mapping)

        // Case: CourseId and Progress
        FilterMatchesPredicate pred1 = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(),
                Optional.of(Progress.ON_TRACK), Optional.empty());
        assertParseSuccess(parser, " crs/CS2103T p/on_track", new FilterCommand(pred1));

        // Case: TGroup and Absence Count
        FilterMatchesPredicate pred2 = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T05")),
                Optional.empty(), Optional.of(10));
        assertParseSuccess(parser, " tg/T05 abs/10", new FilterCommand(pred2));

        // Case: All fields present
        FilterMatchesPredicate pred3 = new FilterMatchesPredicate(
                Optional.of(new CourseId("MA1521")), Optional.of(new TGroup("T01")),
                Optional.of(Progress.AT_RISK), Optional.of(0));
        assertParseSuccess(parser, " crs/MA1521 tg/T01 p/at_risk abs/0", new FilterCommand(pred3));
    }

    @Test
    public void parse_shuffledOrder_success() {
        // EP: Permutation of prefix order (Ensures logic is independent of input sequence)
        FilterMatchesPredicate expectedPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.AT_RISK),
                Optional.of(5));

        // Order: abs -> p -> tg -> crs
        assertParseSuccess(parser, " abs/5 p/at_risk tg/T01 crs/CS2103T", new FilterCommand(expectedPredicate));

        // Order: tg -> crs -> abs -> p
        assertParseSuccess(parser, " tg/T01 crs/CS2103T abs/5 p/at_risk", new FilterCommand(expectedPredicate));
    }

    @Test
    public void parse_robustness_success() {
        // EP: Extra whitespaces and newlines (Optimized for fast-typists/messy input)
        FilterMatchesPredicate expectedPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("MA1521")), Optional.empty(), Optional.empty(), Optional.of(0));

        String input = "    crs/MA1521   \n    abs/0  ";
        assertParseSuccess(parser, input, new FilterCommand(expectedPredicate));
    }
}
