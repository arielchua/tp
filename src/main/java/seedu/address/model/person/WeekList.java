package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a collection of tutorial attendance records for a student across all weeks.
 */
public class WeekList implements WeeklyAttendanceList {
    public static final int NUMBER_OF_WEEKS = 13;
    public static final String MESSAGE_CONSTRAINTS =
            "Week attendance list should be in the format 'W1: Y/N ... W13: Y/N'";
    private final WeeklyAttendance[] weeks;

    /**
     * Constructs a {@code WeekList} with all weeks initialized to not attended.
     */
    public WeekList() {
        this(createDefaultWeeks());
    }

    /**
     * Constructs a {@code WeekList} with the specified attendance records.
     */
    public WeekList(WeeklyAttendance[] weeks) {
        assert weeks.length == NUMBER_OF_WEEKS : "Wrong number of weeks";
        this.weeks = weeks;
    }

    private static WeeklyAttendance[] createDefaultWeeks() {
        WeeklyAttendance[] weekList = new WeeklyAttendance[NUMBER_OF_WEEKS];
        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            weekList[i] = new Week(i + 1);
        }
        return weekList;
    }

    @Override
    public void markWeekAsAttended(int index) {
        assert index >= 0 : "Index must be >= 0";
        assert index < NUMBER_OF_WEEKS : "Index must be < " + NUMBER_OF_WEEKS;
        weeks[index].markAsAttended();
    }

    @Override
    public void markWeekAsAbsent(int index) {
        assert index >= 0 : "Index must be >= 0";
        assert index < NUMBER_OF_WEEKS : "Index must be < " + NUMBER_OF_WEEKS;
        weeks[index].markAsAbsent();
    }
    @Override
    public void markWeekAsDefault(int index) {
        assert index >= 0 : "Index must be >= 0";
        assert index < NUMBER_OF_WEEKS : "Index must be < " + NUMBER_OF_WEEKS;
        weeks[index].markAsDefault();
    }
    @Override
    public void markAsCancelled(int index) {
        assert index >= 0 : "Index must be >= 0";
        assert index < NUMBER_OF_WEEKS : "Index must be < " + NUMBER_OF_WEEKS;
        weeks[index].markAsCancelled();
    }
    @Override
    public void markAsUncancelled(int index) {
        assert index >= 0 : "Index must be >= 0";
        assert index < NUMBER_OF_WEEKS : "Index must be < " + NUMBER_OF_WEEKS;
        weeks[index].markAsUncancelled();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof WeekList otherList)) {
            return false;
        }

        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            if (!this.weeks[i].equals(otherList.weeks[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a copy of this WeekList.
     */
    public WeekList copy() {
        WeeklyAttendance[] copiedWeeks = new WeeklyAttendance[NUMBER_OF_WEEKS];
        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            Week original = (Week) this.weeks[i];
            Week newWeek = new Week(i + 1);
            newWeek.setStatus(Week.Status.valueOf(original.getStatus())); // copy current status
            newWeek.setPrevStatus(original.getPrevStatus()); // copy previous status
            copiedWeeks[i] = newWeek;
        }
        return new WeekList(copiedWeeks);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            result.append(weeks[i].toString()).append(" ");
        }
        return result.toString();
    }

    /**
     * Validates string format.
     */
    public static boolean isValidWeekList(String weekListString) {
        if (weekListString == null) {
            return false;
        }

        String trimmed = weekListString.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length != NUMBER_OF_WEEKS * 2) {
            return false;
        }

        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            String label = parts[i * 2];
            String statusStr = parts[i * 2 + 1];

            // Validate label
            if (!label.equals("W" + (i + 1) + ":")) {
                return false;
            }

            // Validate status using the enum
            try {
                Week.Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return false; // invalid status
            }
        }

        return true;
    }

    /**
     * Uses a String internal representation of Attendancelist to create an attendanceList
     * @return WeekList
     * @throws IllegalValueException
     */
    public static WeekList buildWeekListFromString(String weeklyAttendanceList) throws IllegalValueException {
        requireNonNull(weeklyAttendanceList);
        String trimmed = weeklyAttendanceList.trim();
        if (!WeekList.isValidWeekList(weeklyAttendanceList)) {
            throw new IllegalValueException(WeekList.MESSAGE_CONSTRAINTS);
        }
        String[] parts = trimmed.split("\\s+");
        WeeklyAttendance[] weeks = new WeeklyAttendance[WeekList.NUMBER_OF_WEEKS];
        for (int i = 0; i < WeekList.NUMBER_OF_WEEKS; i++) {
            String status = parts[i * 2 + 1]; // Y / N / A
            Week week = new Week(i + 1);
            switch (status) {
            case "Y":
                week.markAsAttended();
                break;
            case "A":
                week.markAsAbsent();
                break;
            case "N":
                break;
            case "C":
                week.markAsCancelled();
                break;
            default:
                throw new IllegalValueException("Invalid week status: " + status);
            }
            weeks[i] = week;
        }
        return new WeekList(weeks);
    }

    public WeeklyAttendance[] getWeeks() {
        return weeks;
    }

    /**
     * Calculates the week attendance rate as a percentage.
     * @return the attendance percentage.
     */
    public double calculateWeekAttendance() {
        double attended = 0;
        double total = 0;

        for (WeeklyAttendance week : weeks) {
            if (!week.isCancelled()) {
                total++;
                if (week.isAttended()) {
                    attended++;
                }
            }
        }
        return total == 0 ? 0 : attended / total * 100;
    }
    /**
     * Calculates the amount of absences
     * @return the number of absences
     */
    public int calculateWeekAbsence() {
        int count = 0;
        for (WeeklyAttendance week : weeks) {
            if (week.isAbsent()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int compareTo(WeeklyAttendanceList other) {
        return Double.compare(this.calculateWeekAttendance(), other.calculateWeekAttendance());
    }
    public List<TrackerColour> getTrackerColours() {
        List<TrackerColour> colours = new ArrayList<>();
        for (WeeklyAttendance week : weeks) {
            String status = week.getStatus();
            TrackerColour colour = switch (status) {
            case "Y" -> TrackerColour.GREEN;
            case "A" -> TrackerColour.RED;
            case "C" -> TrackerColour.CROSS;
            default -> TrackerColour.GREY;
            };
            colours.add(colour);
        }
        return colours;
    }

    @Override
    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            labels.add("W" + (i + 1));
        }
        return labels;
    }
}
