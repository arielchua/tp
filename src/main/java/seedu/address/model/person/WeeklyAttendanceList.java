package seedu.address.model.person;

/**
 * Collection of student attendance each week
 */
public interface WeeklyAttendanceList extends Comparable<WeeklyAttendanceList> , Trackable {
    /**
     * Marks as attended for specific week.
     * @param index the zero-based index of the week.
     */
    public void markWeekAsAttended(int index);

    /**
     * Marks as unattended for specific week.
     * @param index the zero-based index of the week.
     */
    public void markWeekAsAbsent(int index);

    /**
     * Marks as default for specific week.
     * @param index the zero-based index of the week.
     */
    void markWeekAsDefault(int index);

    /**
     * Marks as cancelled for specific week.
     * @param index the zero-based index of the week.
     */
    public void markAsCancelled(int index);

    /**
     * Calculates the week attendance rate as a percentage.
     * @return the attendance percentage.
     */
    public double calculateWeekAttendance();

    /**
     * Calculates the amount of absences
     * @return the number of absences
     */
    public double calculateWeekAbsence();

    /**
     * @return attendence each week
     */
    WeeklyAttendance[] getWeeks();
}
