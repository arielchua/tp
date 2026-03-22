package seedu.address.model.person;

/**
 * Represents a student's attendance status for a single week.
 */
public class Week implements WeeklyAttendance {
    public static final String MESSAGE_CONSTRAINTS = "Week status must be Y, A or N";

    /**
     * use Letters to represent Status of each Week
     */
    public enum Status {
        Y, // Attended
        A, // Absent
        N // Not marked
    }

    private final int weekNo;
    private Status status;

    /**
     * Constructs a {@code Week} with the specified week number.
     *
     * @param weekNo The week number (must be positive and not exceed {@code WeekList.NUMBER_OF_WEEKS})
     */
    public Week(int weekNo) {
        assert weekNo > 0 : "Invalid week number";
        assert weekNo <= WeekList.NUMBER_OF_WEEKS : "Week number exceeded maximum allowed";
        this.weekNo = weekNo;
        this.status = Status.N;
    }

    @Override
    public void markAsAttended() throws IllegalStateException {
        if (status == Status.Y) {
            throw new IllegalStateException("Week attendance has already been marked as attended");
        }
        status = Status.Y;
    }

    @Override
    public void markAsAbsent() {
        if (status == Status.A) {
            throw new IllegalStateException("Week attendance has already been marked as absent");
        }
        status = Status.A;
    }

    @Override
    public boolean isAttended() {
        return status == Status.Y;
    }

    @Override
    public int getWeek() {
        return weekNo;
    }

    /**
     * Returns:
     * Y = attended
     * A = absent
     * N = default
     */
    public String getStatus() {
        return status.name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Week)) {
            return false;
        }

        Week otherWeek = (Week) other;
        return this.weekNo == otherWeek.weekNo
                && this.status == otherWeek.status;
    }

    @Override
    public String toString() {
        return String.format("W%d: %s", weekNo, getStatus());
    }
}
