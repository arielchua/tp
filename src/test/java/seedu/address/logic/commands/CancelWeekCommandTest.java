package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.TGroup;

public class CancelWeekCommandTest {

    private Model model;
    private CourseId courseId;
    private TGroup tGroup;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        courseId = new CourseId("CS2103T");
        tGroup = new TGroup("T01");
    }

    // =========================
    // SUCCESS CASES
    // =========================

    @Test
    public void execute_validCancel_success() throws Exception {
        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        CommandResult result = cmd.execute(model);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 0));
        assertTrue(result.getFeedbackToUser().contains("cancelled"));
    }

    @Test
    public void execute_cancelMiddleWeek_success() throws Exception {
        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(6));

        cmd.execute(model);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 5));
    }

    @Test
    public void execute_cancelLastWeek_success() throws Exception {
        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(13));

        cmd.execute(model);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 12));
    }

    // =========================
    // FAILURE CASES
    // =========================

    @Test
    public void execute_alreadyCancelled_throwsException() throws Exception {
        model.addCancelledWeek(courseId, tGroup, 0);

        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_invalidWeekTooLarge_throwsException() {
        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(20));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }


    // =========================
    // EDGE CASES
    // =========================

    @Test
    public void execute_multipleCancels_onlyTargetAffected() throws Exception {
        CancelWeekCommand cmd1 =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        CancelWeekCommand cmd2 =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(2));

        cmd1.execute(model);
        cmd2.execute(model);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 0));
        assertTrue(model.isWeekCancelled(courseId, tGroup, 1));
    }

    @Test
    public void execute_cancelTwice_secondFails() throws Exception {
        CancelWeekCommand cmd =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        cmd.execute(model);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    // =========================
    // EQUALS TESTS
    // =========================

    @Test
    public void equals() {
        CancelWeekCommand cmd1 =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        CancelWeekCommand cmd2 =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        CancelWeekCommand cmd3 =
                new CancelWeekCommand(courseId, tGroup, Index.fromOneBased(2));

        assertTrue(cmd1.equals(cmd1));
        assertTrue(cmd1.equals(cmd2));
        assertFalse(cmd1.equals(cmd3));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals(new Object()));
    }
    @Test
    public void integration_cancelUncancelCancel_flow() throws Exception {
        Model model = new ModelManager();
        CourseId c = new CourseId("CS2103T");
        TGroup t = new TGroup("T01");

        CancelWeekCommand cancel =
                new CancelWeekCommand(c, t, Index.fromOneBased(1));

        UnCancelWeekCommand uncancel =
                new UnCancelWeekCommand(c, t, Index.fromOneBased(1));

        cancel.execute(model);
        assertTrue(model.isWeekCancelled(c, t, 0));

        uncancel.execute(model);
        assertFalse(model.isWeekCancelled(c, t, 0));

        cancel.execute(model);
        assertTrue(model.isWeekCancelled(c, t, 0));
    }
}
