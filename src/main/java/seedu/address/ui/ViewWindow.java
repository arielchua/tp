package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * A UI component for displaying a detailed view of a student's information and remarks in a separate window.
 */
public class ViewWindow extends UiPart<Region> {

    private static final String FXML = "ViewWindow.fxml";
    private static final int HEADER_ROW_INDEX = 0;
    private static final int FIRST_REMARK_ROW_INDEX = 1;
    private static final int COL_INDEX_ID = 0;
    private static final int COL_INDEX_DATE = 1;
    private static final int COL_INDEX_TEXT = 2;

    private final Logger logger = LogsCenter.getLogger(getClass());
    private Person person;

    @FXML
    private Label nameLabel;
    @FXML
    private Label studentIdLabel;
    @FXML
    private Label courseIdLabel;
    @FXML
    private Label tGroupLabel;
    @FXML
    private GridPane remarksGrid;

    /**
     * Creates a new {@code ViewWindow} that is intended to be embedded inside
     * the main application window (no separate Stage).
     */
    public ViewWindow() {
        super(FXML);
    }

    /**
     * Sets the person whose details are to be displayed and refreshes the UI.
     *
     * @param person The student to display.
     */
    public void setPerson(Person person) {
        requireNonNull(person);
        logger.info("Updating ViewWindow to display: " + person.getName());
        this.person = person;
        updateDisplay();
    }

    /**
     * Checks if the window is currently displaying information for the given {@code Person}.
     * This is used to determine if the window needs an auto-refresh after a command execution.
     *
     * @param other The person to check against.
     * @return True if the current person being viewed matches the other person.
     */
    public boolean isViewing(Person other) {
        return person != null && person.equals(other);
    }

    /**
     * Updates all UI components with the data from the current person.
     */
    private void updateDisplay() {
        assert person != null : "Person cannot be null here";
        updateMetadata();
        refreshRemarksGrid();
    }

    /**
     * Updates the text labels in the header section with the person's basic information.
     */
    private void updateMetadata() {
        assert person != null : "Person should not be null when updating metadata";
        nameLabel.setText(person.getName().fullName);
        studentIdLabel.setText(person.getStudentId().value);
        courseIdLabel.setText(person.getCourseId().value);
        tGroupLabel.setText(person.getTGroup().value);
    }

    /**
     * Clears existing remark rows and repopulates the grid with the current remarks.
     * The header row is defined in FXML and preserved.
     */
    private void refreshRemarksGrid() {
        assert person != null : "Person should not be null when refreshing remarks";
        clearRemarksOnly();
        int rowIndex = FIRST_REMARK_ROW_INDEX;
        for (Remark remark : person.getRemarks()) {
            addRemarkRow(remark, rowIndex);
            rowIndex++;
        }
    }

    /**
     * Adds a single remark as a new row in the grid.
     *
     * @param remark The remark data to add.
     * @param rowIndex The index of the row where the remark should be placed.
     */
    private void addRemarkRow(Remark remark, int rowIndex) {
        int displayIndex = rowIndex - FIRST_REMARK_ROW_INDEX + 1;
        Label indexLabel = createCellLabel(String.valueOf(displayIndex), "index-cell");
        Label dateLabel = createCellLabel(remark.getDate().toString(), "date-cell");
        Label remarkLabel = createCellLabel(remark.getText(), "remark-cell");
        remarkLabel.setWrapText(true);

        remarksGrid.add(indexLabel, COL_INDEX_ID, rowIndex);
        remarksGrid.add(dateLabel, COL_INDEX_DATE, rowIndex);
        remarksGrid.add(remarkLabel, COL_INDEX_TEXT, rowIndex);
    }

    /**
     * Creates a styled {@code Label} for a grid cell.
     *
     * @param text The text for the label.
     * @param styleClass The CSS style class to apply.
     * @return A new {@code Label} instance.
     */
    private Label createCellLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    /**
     * Clears the current view and resets UI components.
     */
    public void clear() {
        this.person = null;
        nameLabel.setText("");
        studentIdLabel.setText("");
        courseIdLabel.setText("");
        tGroupLabel.setText("");
        clearRemarksOnly();
    }

    /**
     * Clears only the remark rows from the grid, preserving the header.
     */
    private void clearRemarksOnly() {
        remarksGrid.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            // If rowIndex is null, it's considered to be in the first row (header).
            // This is a defensive check for nodes added without an explicit row index.
            int effectiveRowIndex = rowIndex == null ? HEADER_ROW_INDEX : rowIndex;
            return effectiveRowIndex >= FIRST_REMARK_ROW_INDEX;
        });
    }
}
