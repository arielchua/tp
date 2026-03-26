package seedu.address.ui;

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
     * Orchestrates the update of all UI components with the data from the current person.
     */
    private void updateDisplay() {
        if (person == null) {
            return;
        }
        updateMetadata();
        resetAndPopulateGrid();
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
     * Clears existing remarks and re-populates the grid with headers and current remarks.
     */
    private void resetAndPopulateGrid() {
        remarksGrid.getChildren().clear();
        addHeaderRow();
        int rowIndex = 1;
        for (Remark remark : person.getRemarks()) {
            addRemarkRow(remark, rowIndex++);
        }
    }

    /**
     * Adds the "Date" and "Remark" header labels to the first row of the grid.
     */
    private void addHeaderRow() {
        Label dateHeader = new Label("Date");
        dateHeader.getStyleClass().add("column-header");
        dateHeader.setMaxWidth(Double.MAX_VALUE);

        Label remarkHeader = new Label("Remark");
        remarkHeader.getStyleClass().add("column-header");
        remarkHeader.setMaxWidth(Double.MAX_VALUE);

        remarksGrid.add(dateHeader, 0, 0);
        remarksGrid.add(remarkHeader, 1, 0);
    }

    /**
     * Adds a single remark as a new row in the grid.
     *
     * @param remark The remark data to add.
     * @param rowIndex The index of the row where the remark should be placed.
     */
    private void addRemarkRow(Remark remark, int rowIndex) {
        Label dateLabel = new Label(remark.getDate().toString());
        dateLabel.getStyleClass().add("date-cell");
        dateLabel.setMaxWidth(Double.MAX_VALUE);

        Label remarkLabel = new Label(remark.getText());
        remarkLabel.getStyleClass().add("remark-cell");
        remarkLabel.setWrapText(true);
        remarkLabel.setMaxWidth(Double.MAX_VALUE);

        remarksGrid.add(dateLabel, 0, rowIndex);
        remarksGrid.add(remarkLabel, 1, rowIndex);
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
