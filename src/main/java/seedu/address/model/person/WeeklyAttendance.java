package seedu.address.model.person;

/**
 * Represents a weekly Tutorial's attendance status.
 */
public interface WeeklyAttendance {
    /**
     * Marks this week as attended.
     */
    public void markAsAttended();

    /**
     * Marks this week as not attended.
     */
    public void markAsAbsent();

    /**
     * Marks this week to be default.
     */
    public void markAsDefault();

    void markAsCancelled() throws IllegalStateException;

    /**
     * Returns whether this week has been marked as attended.
     *
     * @return {@code true} if the week has been marked as attended;
     *         {@code false} otherwise
     */
    public boolean isAttended();

    /**
     * Returns whether this week has been marked as cancelled.
     *
     * @return {@code true} if the week has been marked as cancelled;
     *         {@code false} otherwise
     */
    public boolean isCancelled();

    public int getWeek();

    /**
     * @return the attendance status of the week session
     */
    public String getStatus();
}
