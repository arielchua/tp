package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private static final String WELCOME_MESSAGE = """
            Welcome to TeachAssist!
            Manage student attendance, progress, and remarks across multiple courses in one place.
            Type 'help' to see the list of available commands.""";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final Stage primaryStage;
    private final Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private final HelpWindow helpWindow;
    private CommandBox commandBox;
    private final ViewWindow viewWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane viewWindowPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
        viewWindow = new ViewWindow();
    }

    /**
     * Returns the primary stage of the application.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param menuItem       the menu item to configure
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * Workaround for a bug in JavaFX [JDK-8131666], where function-key events are
         * consumed by TextInputControl (TextField, TextArea). This prevents accelerators
         * like F1 from working when the focus is in the CommandBox or ResultDisplay.
         *
         * We use an event filter to capture such key events and trigger the action
         * manually. This ensures that accelerators work globally, even when focus
         * is on a text input control.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        // Open detail view when a student row is clicked
        personListPanel.getPersonListView().setOnMouseClicked(event -> {
            Person selected = personListPanel.getPersonListView().getSelectionModel().getSelectedItem();
            if (selected != null) {
                handleView(selected);
            }
        });

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
        resultDisplay.setFeedbackToUser(WELCOME_MESSAGE);

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    /**
     * Opens the view window with the specified person's details,
     * or focuses on it if it is already opened.
     *
     * @param person the person to display
     */
    @FXML
    public void handleView(Person person) {
        assert person != null : "Person to view cannot be null";
        // Set the person data on the embedded view and add the UI to the placeholder
        viewWindow.setPerson(person);
        if (viewWindowPlaceholder.getChildren().isEmpty()) {
            viewWindowPlaceholder.getChildren().add(viewWindow.getRoot());
        }
        // Ensure the person list selection reflects the person being viewed.
        // This keeps the UI selection (blue highlight) in sync with the embedded view.
        if (personListPanel != null && personListPanel.getPersonListView() != null) {
            personListPanel.getPersonListView().getSelectionModel().select(person);
        }
    }

    /**
     * Shows this main window.
     */
    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        // clear embedded view window if present
        if (viewWindowPlaceholder != null) {
            viewWindow.clear();
            viewWindowPlaceholder.getChildren().clear();
        }
        primaryStage.hide();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            // Process various command outcomes
            handleCommandResult(commandResult);

            // SLAP: Extract the complex view-refresh logic to a helper
            updateViewWindowAfterCommand();

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }


    /**
     * Processes the outcome of a command execution.
     */
    private void handleCommandResult(CommandResult commandResult) {
        if (commandResult.shouldShowView()) {
            handleView(commandResult.getPersonToView());
        }

        if (commandResult.isHelpRequest()) {
            handleHelp();
        }

        if (commandResult.isExit()) {
            handleExit();
        }
    }

    /**
     * Updates or clears the view window based on the current state of the filtered person list.
     */
    private void updateViewWindowAfterCommand() {
        if (viewWindowPlaceholder.getChildren().isEmpty()) {
            return;
        }

        findUpdatedPersonInList().ifPresentOrElse(
                this::syncListSelection,
                this::clearViewWindow
        );
    }

    /**
     * Searches the filtered person list for the person currently being viewed
     * and returns an {@code Optional} containing the updated person if found.
     *
     * @return An {@code Optional} with the updated person, or empty if not found.
     */
    private java.util.Optional<Person> findUpdatedPersonInList() {
        return logic.getFilteredPersonList().stream()
                .filter(viewWindow::isViewing)
                .findFirst();
    }

    /**
     * Synchronizes the person list selection and view window with the given person.
     *
     * @param person The person to display and select.
     */
    private void syncListSelection(Person person) {
        viewWindow.setPerson(person);
        assert personListPanel != null : "personListPanel should not be null";
        assert personListPanel.getPersonListView() != null : "personListPanel's ListView should not be null";
        personListPanel.getPersonListView().getSelectionModel().select(person);
    }

    /**
     * Clears the view window and its placeholder.
     */
    private void clearViewWindow() {
        viewWindow.clear();
        viewWindowPlaceholder.getChildren().clear();
        // Also clear selection in the person list to avoid stale blue highlight
        assert personListPanel != null : "personListPanel should not be null";
        assert personListPanel.getPersonListView() != null : "personListPanel's ListView should not be null";
        personListPanel.getPersonListView().getSelectionModel().clearSelection();
    }
}
