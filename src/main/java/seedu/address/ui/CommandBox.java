package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.DeleteCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            // handle UI when delete command is trying to be executed
            if (isValidDeleteCommand(commandText)) {
                boolean confirmed = showDeleteConfirmationPopup();

                if (!confirmed) {
                    return;
                }
            }

            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    /**
     * Returns true if the given command text is a valid delete command.
     */
    private boolean isValidDeleteCommand(String commandText) {
        String trimmedCommand = commandText.trim();

        if (!(trimmedCommand.equals(DeleteCommand.COMMAND_WORD)
            || trimmedCommand.startsWith(DeleteCommand.COMMAND_WORD + " "))) {
            return false;
        }

        String remainder = trimmedCommand.substring(DeleteCommand.COMMAND_WORD.length()).trim();

        try {
            new DeleteCommandParser().parse(remainder);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Shows a confirmation popup for delete command.
     *
     * @return true if user clicks OK, false otherwise.
     */
    private boolean showDeleteConfirmationPopup() {
        DeleteConfirmationWindow deleteConfirmationWindow = new DeleteConfirmationWindow();
        deleteConfirmationWindow.setMessage("Are you sure you want to delete this student?");
        return deleteConfirmationWindow.showAndWait();
    }
}
