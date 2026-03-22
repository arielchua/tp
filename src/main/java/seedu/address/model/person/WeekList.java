package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;

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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof WeekList)) {
            return false;
        }

        WeekList otherList = (WeekList) other;
        for (int i = 0; i < NUMBER_OF_WEEKS; i++) {
            if (this.weeks[i].isAttended() != otherList.weeks[i].isAttended()) {
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
            if (original.isAttended()) {
                newWeek.markAsAttended();
            }
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
            String status = parts[i * 2 + 1];

            if (!label.equals("W" + (i + 1) + ":")) {
                return false;
            }

            if (!status.equals("Y") && !status.equals("N") && !status.equals("A")) {
                return false;
            }
        }
        return true;
    }

    public WeeklyAttendance[] getWeeks() {
        return weeks;
    }

    /**
     * Calculates the week attendance rate as a percentage.
     * @return the attendance percentage.
     */
    public double calculateWeekAttendance() {
        double count = 0;
        for (WeeklyAttendance week : weeks) {
            if (week.isAttended()) {
                count++;
            }
        }
        return count / NUMBER_OF_WEEKS * 100;
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
