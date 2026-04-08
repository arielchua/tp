package seedu.address.model.person;

import java.util.Objects;

/**
 * Represents a student's attendance status for a single week.
 */
public class Week implements WeeklyAttendance {
    //For marking attendance
    public static final String MESSAGE_CONSTRAINTS =
            "Week status must be 'Y' (attended), 'A' (absent), or 'N' (not marked).";

    /**
     * use Letters to represent Status of each Week
     */
    public enum Status {
        Y, // Attended
        A, // Absent
        N, // Not marked
        C; // Cancelled

        /**
         * Convert a String into the Status Enum
         * @param value string to be converted to Enum
         * @return Status Enum that string is converted to
         */
        public static Status fromString(String value) {
            try {
                return Status.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
            }
        }
    }

    private final int weekNo;
    private Status status;
    private Status prevStatus = Status.N;

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
    private void markErrorCheck() {
        if (status == Status.C) {
            throw new IllegalStateException("Cannot modify a cancelled week");
        }
    }

    @Override
    public void markAsAttended() throws IllegalStateException {
        if (status == Status.Y) {
            throw new IllegalStateException("Week attendance has already been marked as attended");
        }
        markErrorCheck();
        status = Status.Y;
    }

    @Override
    public void markAsAbsent() throws IllegalStateException {
        if (status == Status.A) {
            throw new IllegalStateException("Week attendance has already been marked as absent");
        }
        markErrorCheck();
        status = Status.A;
    }
    @Override
    public void markAsDefault() throws IllegalStateException {
        if (status == Status.N) {
            throw new IllegalStateException("Week attendance is already default");
        }
        markErrorCheck();
        status = Status.N;
    }
    @Override
    public void markAsCancelled() throws IllegalStateException {
        if (status == Status.C) {
            throw new IllegalStateException("Week attendance is already cancelled");
        }
        prevStatus = status;
        status = Status.C;
    }
    @Override
    public void markAsUncancelled() throws IllegalStateException {
        if (status != Status.C) {
            throw new IllegalStateException("Week attendance is not cancelled");
        }
        status = prevStatus;
    }

    /**
     * Getter method for preStatus.
     * @return Status that might have been removed by cancel
     */
    public Status getPrevStatus() {
        return prevStatus;
    }

    /**
     * Setter Method for precStatus
     * @param oldStatus the prevStatus inputted
     */
    public void setPrevStatus(Status oldStatus) {
        prevStatus = oldStatus;
    }
    @Override
    public boolean isAttended() {
        return status == Status.Y;
    }
    @Override
    public boolean isCancelled() {
        return status == Status.C;
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
    public Week.Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
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
    public int hashCode() {
        return Objects.hash(weekNo, status);
    }
    @Override
    public String toString() {
        return String.format("W%d: %s", weekNo, getStatus().toString());
    }

    @Override
    public boolean isAbsent() {
        return this.status == Status.A;
    }
}
