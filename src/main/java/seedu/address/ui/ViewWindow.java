package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
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

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final Stage stage;
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
     * Creates a new {@code ViewWindow} with its own {@code Stage}.
     */
    public ViewWindow() {
        super(FXML);
        stage = new Stage();
        stage.setTitle("View - Student Details");
        stage.setScene(new Scene(getRoot()));
    }

    /**
     * Sets the person whose details are to be displayed and refreshes the UI.
     *
     * @param person The student to display.
     */
    public void setPerson(Person person) {
        requireNonNull(person);
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
        return person != null && person.isSamePerson(other);
    }

    /**
     * Updates all UI components with the data from the current person.
     */
    private void updateDisplay() {
        updateMetadata();
        refreshRemarksGrid();
    }

    /**
     * Updates the text labels in the header section with the person's basic information.
     */
    private void updateMetadata() {
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
        remarksGrid.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            int effectiveRowIndex = rowIndex == null ? HEADER_ROW_INDEX : rowIndex;
            return effectiveRowIndex >= FIRST_REMARK_ROW_INDEX;
        });

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
        Label indexLabel = new Label(String.valueOf(displayIndex));
        indexLabel.getStyleClass().add("index-cell");
        indexLabel.setMaxWidth(Double.MAX_VALUE);

        Label dateLabel = new Label(remark.getDate().toString());
        dateLabel.getStyleClass().add("date-cell");
        dateLabel.setMaxWidth(Double.MAX_VALUE);

        Label remarkLabel = new Label(remark.getText());
        remarkLabel.getStyleClass().add("remark-cell");
        remarkLabel.setWrapText(true);
        remarkLabel.setMaxWidth(Double.MAX_VALUE);
        remarksGrid.add(indexLabel, 0, rowIndex);
        remarksGrid.add(dateLabel, 1, rowIndex);
        remarksGrid.add(remarkLabel, 2, rowIndex);
    }

    /**
     * Shows the view window and centers it on the screen.
     */
    public void show() {
        logger.fine("Showing view window.");
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Hides the view window.
     */
    public void hide() {
        stage.hide();
    }

    /**
     * Returns true if the view window is currently being shown.
     *
     * @return True if the window is showing, false otherwise.
     */
    public boolean isShowing() {
        return stage.isShowing();
    }

    /**
     * Focuses on the view window to bring it to the foreground.
     */
    public void focus() {
        stage.requestFocus();
    }
}
