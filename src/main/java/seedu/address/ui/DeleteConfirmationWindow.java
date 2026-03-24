package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for a delete confirmation popup.
 */
public class DeleteConfirmationWindow extends UiPart<Stage> {

    private static final String FXML = "DeleteConfirmationWindow.fxml";

    private boolean isConfirmed = false;

    @FXML
    private Label confirmationMessage;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    /**
     * Creates a new DeleteConfirmationWindow.
     *
     * @param root Stage to use as the root of the window.
     */
    public DeleteConfirmationWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new DeleteConfirmationWindow.
     */
    public DeleteConfirmationWindow() {
        this(new Stage());
    }

    /**
     * Sets the message shown in the popup.
     */
    public void setMessage(String message) {
        confirmationMessage.setText(message);
    }

    /**
     * Shows the popup and waits for user response.
     *
     * @return true if user clicked OK, false otherwise.
     */
    public boolean showAndWait() {
        getRoot().showAndWait();
        getRoot().centerOnScreen();
        return isConfirmed;
    }

    @FXML
    private void handleOk() {
        isConfirmed = true;
        getRoot().close();
    }

    @FXML
    private void handleCancel() {
        isConfirmed = false;
        getRoot().close();
    }
}
