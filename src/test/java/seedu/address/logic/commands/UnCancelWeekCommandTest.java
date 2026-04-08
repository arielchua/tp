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

public class UnCancelWeekCommandTest {

    private Model model;
    private CourseId courseId;
    private TGroup tGroup;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        courseId = new CourseId("CS2103T");
        tGroup = new TGroup("T01");
    }

    @Test
    public void execute_validUncancel_success() throws Exception {
        // simulate cancelled week
        model.addCancelledWeek(courseId, tGroup, 0);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        CommandResult result = cmd.execute(model);

        assertFalse(model.isWeekCancelled(courseId, tGroup, 0));
        assertTrue(result.getFeedbackToUser().contains("uncancelled"));
    }

    @Test
    public void execute_uncancelMiddleWeek_success() throws Exception {
        model.addCancelledWeek(courseId, tGroup, 5);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(6));

        cmd.execute(model);

        assertFalse(model.isWeekCancelled(courseId, tGroup, 5));
    }

    @Test
    public void execute_uncancelLastWeek_success() throws Exception {
        model.addCancelledWeek(courseId, tGroup, 12);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(13));

        cmd.execute(model);

        assertFalse(model.isWeekCancelled(courseId, tGroup, 12));
    }

    @Test
    public void execute_weekNotCancelled_throwsException() {
        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_invalidWeekTooLarge_throwsException() {
        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(20));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_wrongCourseId_throwsException() {
        model.addCancelledWeek(courseId, tGroup, 0);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(new CourseId("CS9999"),
                        tGroup,
                        Index.fromOneBased(1));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_wrongTGroup_throwsException() {
        model.addCancelledWeek(courseId, tGroup, 0);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId,
                        new TGroup("T99"),
                        Index.fromOneBased(1));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_multipleCancelledWeeks_onlyTargetUncancelled() throws Exception {
        model.addCancelledWeek(courseId, tGroup, 0);
        model.addCancelledWeek(courseId, tGroup, 1);
        model.addCancelledWeek(courseId, tGroup, 2);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(2));

        cmd.execute(model);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 0));
        assertFalse(model.isWeekCancelled(courseId, tGroup, 1));
        assertTrue(model.isWeekCancelled(courseId, tGroup, 2));
    }

    @Test
    public void execute_uncancelTwice_secondFails() throws Exception {
        model.addCancelledWeek(courseId, tGroup, 0);

        UnCancelWeekCommand cmd =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        cmd.execute(model);

        // second time should fail
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void equals() {
        UnCancelWeekCommand cmd1 =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        UnCancelWeekCommand cmd2 =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1));

        UnCancelWeekCommand cmd3 =
                new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(2));

        assertTrue(cmd1.equals(cmd1));
        assertTrue(cmd1.equals(cmd2));
        assertFalse(cmd1.equals(cmd3));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals(new Object()));
    }

    @Test
    public void integration_cancelThenUncancel_success() throws Exception {
        Model model = new ModelManager();
        CourseId courseId = new CourseId("CS2103T");
        TGroup tGroup = new TGroup("T01");

        model.addCancelledWeek(courseId, tGroup, 0);

        assertTrue(model.isWeekCancelled(courseId, tGroup, 0));

        new UnCancelWeekCommand(courseId, tGroup, Index.fromOneBased(1))
                .execute(model);

        assertFalse(model.isWeekCancelled(courseId, tGroup, 0));
    }

    @Test
    public void integration_partialUncancel_success() throws Exception {
        Model model = new ModelManager();
        CourseId c = new CourseId("CS2103T");
        TGroup t = new TGroup("T01");

        model.addCancelledWeek(c, t, 0);
        model.addCancelledWeek(c, t, 1);

        new UnCancelWeekCommand(c, t, Index.fromOneBased(1)).execute(model);

        assertFalse(model.isWeekCancelled(c, t, 0));
        assertTrue(model.isWeekCancelled(c, t, 1));
    }
}
