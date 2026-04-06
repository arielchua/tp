---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TeachAssist Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project was forked from [se-edu/addressbook-level3](https://github.com/se-edu/addressbook-level3) under the MIT License.

### **Third-Party Libraries**

- **JavaFX** — for building the graphical user interface. ([openjfx.io](https://openjfx.io))
- **Jackson** — for JSON data serialization and deserialization. ([github.com/FasterXML/jackson](https://github.com/FasterXML/jackson))
- **JUnit 5** — for unit and integration testing. ([junit.org/junit5](https://junit.org/junit5))
  
### **Development Tools**

- **Gradle** — for build automation and dependency management. ([gradle.org](https://gradle.org))
- **GitHub Pages** — for hosting project documentation. ([pages.github.com](https://pages.github.com))
- **PlantUML** — for generating UML diagrams used in documentation. ([plantuml.com](https://plantuml.com))
- **MarkBind** — for authoring and publishing the user and developer guides. ([markbind.org](https://markbind.org))
- **GitHub Actions** — for continuous integration and automated build testing. ([github.com/features/actions](https://github.com/features/actions))

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="600" />


The `Model` component,

* stores the TeachAssist data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed', e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
* does not depend on any of the other three components, as the `Model` represents data entities of the domain and they should make sense on their own without depending on other components.

<box type="info" seamless>

**Note:** Unlike the original AB3 model, TeachAssist uses student-specific fields and records instead of tag-based contact classification. Each `Person` stores student-related fields such as `Name`, `CourseId`, `Email`, `StudentId`, `TGroup`, `Tele`, `WeekList`, `Progress`, and a list of `Remark` objects.

<puml src="diagrams/BetterModelClassDiagram.puml" width="600" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both TeachAssist data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------
## **Key Feature Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Feature: Add Student

#### Overview

Describe the purpose of the `add` command and how it differs from the original AB3 implementation. State that TeachAssist uses student-specific fields instead of generic contact-oriented fields.

#### Student data model

Describe the student-related field classes involved in creating a student record, such as `Name`, `StudentId`, `CourseId`, `TGroup`, `Email`, `Tele`, and any other relevant attributes stored in `Person`.

Relevant diagram: Class diagram showing `Person` and the student-related field classes.

#### Implementation

Explain how `AddCommandParser` parses the user input, validates each field, constructs the `Person` object, and passes it to `AddCommand` for insertion into the model.

#### Duplicate handling

Explain how TeachAssist checks whether a student record already exists before adding it, and describe which fields are used to determine duplicates in the current implementation.

### Feature: Delete Student

#### Overview

Describe the purpose of the `delete` command and explain that it has been extended beyond the original AB3 version.

#### Supported delete modes

Explain that TeachAssist supports deletion by displayed index and by student details. State the exact student details required for detail-based deletion.

#### Confirmation workflow

Explain that deletion does not happen immediately after a valid delete command. Describe how TeachAssist first identifies the target student, prompts the user for confirmation, and then proceeds only if the user confirms.

Relevant diagram: Activity diagram showing the branching delete flow, including deletion by index, deletion by student details, and confirmation handling.

#### Implementation

Explain how the parser distinguishes between the supported delete formats, what validation is performed in the parser, what validation is deferred to command execution, and how the final deletion is carried out after confirmation.

Relevant diagram: Sequence diagram showing the successful delete flow through parser, command, model, and confirmation logic.

### Feature: Update Progress

#### Overview

Describe the purpose of the `updateprogress` command and how it helps TAs track a student’s academic standing or follow-up status.

#### Progress representation

Explain the valid progress states supported by TeachAssist and describe how the progress status is represented in the model.

#### Implementation

Explain how the command identifies the target student, validates the new progress value, updates the relevant `Person` object, and replaces the old record in the model.

Relevant diagram: Sequence diagram showing how the command identifies the student, validates the progress status, and updates the model.

#### UI integration

Describe how progress is displayed in the UI.

### Feature: Mark Attendance

#### Overview

Describe the purpose of the `markattendance` command and how it supports tutorial or class attendance tracking in TeachAssist.

#### Attendance representation

Explain how attendance information is represented in the model. State whether attendance is stored as a count, a list, a status field, or another structure.

#### Implementation

Explain how the command parses the attendance input, identifies the target student, validates the attendance-related value, updates the student record, and commits the updated record back into the model.

Relevant diagram: Sequence diagram showing how attendance input is parsed, validated, and recorded for the target student.

### Feature: Remark Command

#### Overview

Describe the purpose of the `remark` command and how it allows TAs to store comment-like notes on individual students.

#### Remark representation

Explain how remarks are represented in the model, such as whether each remark is stored as text alone or as a richer object containing metadata like date or timestamp.

Relevant diagram: Class diagram snippet showing how remarks are associated with a student record.

#### Implementation

Explain how the `remark` command parses the target student and remark text, constructs the new remark, adds it to the student record, and updates the modified student in the model.

Relevant diagram: Sequence diagram showing how the remark is parsed, created, and attached to the target student.

### Feature: View Command

#### Overview

Describe the purpose of the `view` command and what additional student information it allows users to inspect.

#### UI behaviour

Explain what the user sees when the command is executed, such as whether a separate window, popup, or detailed panel is shown, and what information is included in the display.

#### Implementation

Explain how the command identifies the target student, retrieves the required details and remarks from the model, and passes the data to the relevant UI component for display.

Relevant diagram: Sequence diagram showing how the selected student’s details and remarks are retrieved and passed to the UI for display.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a … | I want to … | So that I can… |
|----------|--------|-------------|---------------|
| `**` | new user | see a welcome message on first launch | know how to get started |
| `**` | new user | view a help command listing all available commands | understand what commands the system supports |
| `**` | new user | view preloaded sample student data | understand how student records are structured |
| `**` | new user | purge all sample data | start working with my real student records |
| `***` | TA | add a student with fields such as name, student ID, course ID, tutorial group, email, and Telegram handle | maintain complete and structured student records |
| `***` | TA | edit a student’s details | keep student records accurate and up to date |
| `***` | TA | delete a student by index | quickly remove an incorrect or outdated student record |
| `***` | TA | delete a student by student details | remove a specific student even when I do not want to rely on the displayed index |
| `***` | careful TA | be asked to confirm before deleting a student | avoid accidentally deleting the wrong student record |
| `***` | TA | view the full student list | get an overview of all the students I am managing |
| `**` | TA handling multiple classes | filter or narrow down the displayed student list | focus on the relevant group of students more quickly |
| `**` | TA handling multiple classes | identify students using course ID and tutorial group in addition to name | avoid confusion between students from different classes or students with similar names |
| `***` | TA tracking student performance | update a student’s progress status | quickly identify which students are on track or need support |
| `**` | TA preparing for class | view a student’s progress status in the UI | understand the student’s standing at a glance |
| `***` | TA taking tutorial attendance | mark attendance for a student | keep a record of who attended class |
| `***` | TA who conducts consultations | add remarks to a student’s record | remember important discussion points and follow-up actions |
| `***` | TA who conducts consultations | view a student’s remarks and details | prepare for future consultations more effectively |
| `**` | TA managing many students | keep remarks together with each student record | avoid scattering notes across separate apps or documents |
| `**` | TA managing multiple tutorial groups | keep all students across different courses and tutorial groups in one application | avoid maintaining multiple spreadsheets or lists |
| `**` | careful TA | receive clear error messages when a command format is invalid | correct mistakes quickly |
| `**` | careful TA | be prevented from adding duplicate student records | maintain clean and consistent data |
| `**` | TA | clear the current filter | return to the full student list after narrowing it down |


### Use cases

**Use Case: UC01 – Add Student**<br>
**Actor:** User<br>
**MSS:**
1. User enters the command to add a student.
2. User provides the student’s name, student ID, course, tutorial group, and optionally a Telegram username.
3. TeachAssist validates the input.
4. TeachAssist creates the student record.
5. TeachAssist adds the student to the student list.
6. TeachAssist confirms that the student has been added.
7. Use case ends.

**Extensions:**

* 3a. The input format is invalid.
    * 3a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 3b. A student with the same student ID already exists.
    * 3b1. TeachAssist rejects the command.
    * 3b2. TeachAssist informs the user that the student already exists.
    * Use case ends.

**Use Case: UC02 – Edit Student**<br>
**Actor:** User<br>
**MSS:**
1. User issues the `edit` command specifying a target student and the fields to update.
2. TeachAssist validates the input and the existence of the target student.
3. TeachAssist updates the student record with the provided changes.
4. TeachAssist confirms that the student has been updated.
5. Use case ends.

**Extensions:**

* 2a. The input format is invalid.
    * 2a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 2b. The specified student does not exist.
    * 2b1. TeachAssist informs the user that the specified student could not be found.
    * Use case ends.

**Use Case: UC05 – Mark Attendance**<br>
**Actor:** User<br>
**MSS:**

1. User issues a command to mark attendance for a specific student and tutorial week.
2. TeachAssist updates the attendance record for the specified student and week.
3. TeachAssist confirms the updated attendance status.
4. Use case ends.

**Extensions:**

* 2a. The specified student does not exist.
    * 2a1. TeachAssist informs the user and aborts the operation.
    * Use case ends.
* 2b. The specified week is invalid (out of range or cancelled).
    * 2b1. TeachAssist informs the user and aborts the operation.
    * Use case ends.

**Use Case: UC06 – Add Academic Notes**<br>
**Actor:** User<br>
**MSS:**

1. User selects a student.
2. User enters the add note command.
3. TeachAssist requests the note content.
4. User enters the note.
5. TeachAssist attaches the note with a timestamp to the student profile.
6. TeachAssist confirms the addition.
7. Use case ends.


**Use Case: UC08 – Update Student Progress Status**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to update a student’s progress status to a specific status.
3. TeachAssist updates the student progress status.
4. TeachAssist confirms the update.
5. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 1c. The specified progress status is invalid.
    * 1c1. TeachAssist informs the user of the valid progress statuses.
    * Use case ends.

**Use Case: UC09 – View Student Details**<br>
**Actor:** User<br>
**MSS:**

1. User issues the `view` command with a student index.
2. TeachAssist displays the selected student's detailed information and remark entries in the UI.
3. Use case ends.

**Extensions**

* 1a. The specified index is out of range.
    * 1a1. TeachAssist informs the user that the specified student does not exist and aborts the operation.
    * Use case ends.

**Use Case: UC10 – View Help Command** <br>
**Actor:** User <br>
**MSS:**

1. User enters the help command.
2. TeachAssist retrieves the list of supported commands.
3. TeachAssist displays the command list with brief descriptions.
4. User reviews the available commands.
5. Use case ends

**Use Case: UC11 – Delete Student** <br>
**Actor:** User <br>
**MSS:**

1. User enters a command to delete a student.
2. TeachAssist validates the command and identifies the student record to be deleted.
3. TeachAssist displays the student details and asks the user to confirm the deletion.
4. User enters a confirmation response.
5. TeachAssist deletes the student record from the system.
6. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 2a. The specified student does not exist.
    * 2a1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 4a. The user declines the deletion.
    * 4a1. TeachAssist cancels the deletion.
    * Use case ends.
* 4b. The confirmation response is invalid.
    * 4b1. TeachAssist informs the user to enter a valid confirmation response.
    * Use case ends.
 
**Use Case: UC12 – View Student List** <br>
**Actor:** User <br>
**MSS:**

1.User enters the command to view all students.
2. TeachAssist retrieves all student records.
3. TeachAssist displays the list of students.
4. Use case ends.

**Use Case: UC13 – List Students (reset view / clear filters)** <br>
**Actor:** User <br>
**MSS:**

1. User enters the `list` command.
2. TeachAssist displays the full student list (any active filters are cleared for the displayed view).
3. Use case ends.

**Use Case: UC14 – Filter Student List** <br>
**Actor:** User <br>
**MSS:**

1. User enters a `filter` command with one or more criteria (e.g., course, tutorial group, progress state, attendance threshold).
2. TeachAssist applies the filter and updates the displayed student list to show only students matching all provided criteria.
3. TeachAssist displays feedback summarising the active filter and the number of matching students.
4. Use case ends.


**Extensions :**

* 2a. A required parameter value is missing for one of the criteria (e.g., `crs/` with no course id).
    * 2a1. TeachAssist informs the user of the missing value and shows correct usage.
    * Use case ends.
* 2b. A provided criterion has an invalid format (e.g., malformed tutorial group `tg/@@@`).
    * 2b1. TeachAssist informs the user about the invalid format for that criterion.
    * Use case ends.
* 2c. A provided progress value is not one of the supported statuses.
    * 2c1. TeachAssist informs the user of valid progress values and rejects the filter.
    * Use case ends.
* 2d. The absence count (`abs/`) is not a non-negative integer.
    * 2d1. TeachAssist informs the user that absence must be a non-negative integer.
    * Use case ends.
* 2e. The combination of criteria is valid but yields no matches.
    * 2e1. TeachAssist displays an empty list and a message indicating that no students match the filter.
    * Use case ends.
* 2f. Multiple criteria are supplied.
    * 2f1. TeachAssist combines criteria using logical AND semantics and updates the list accordingly.
    * Use case ends.
* 2g. The user issues `filter` while another view or filter is active.
    * 2g1. TeachAssist replaces the currently displayed view with the new filtered results.
    * Use case ends.

**Use Case: UC15 – Find Students** <br>
**Actor:** User <br>
**MSS:**

1. User enters a `find` command with one or more keywords (e.g., parts of a name or student id).
2. TeachAssist searches the student records for matches and updates the displayed list to show matching students.
3. TeachAssist displays feedback indicating the number of students found and the search terms used.
4. Use case ends.

**Extensions:**

* 2a. The `find` command contains illegal characters or is an empty query.
    * 2a1. TeachAssist informs the user of the correct syntax for the `find` command.
    * Use case ends.
* 2b. No students match the query.
    * 2b1. TeachAssist displays an empty list and a message such as "No students found for: <query>".
    * Use case ends.

### Non-Functional Requirements
1. Performance
    * The system should respond to user commands within 1 second under normal usage.

2. Portability
    * The system should run on Windows, macOS, and Linux environments supporting Java.
      
4. Data Persistence
    * Student records must be saved to persistent storage.
    * Data should remain available after the system is restarted


*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. _{Fill in}_

    2. _{Fill in expected behaviour}_

2. Saving window preferences

    1. _{Fill in}_

    2. _{Fill in expected behaviour}_

3. Shutdown

    1. _{Fill in}_

    2. _{Fill in expected behaviour}_

### Help command (`help`) — manual tests

1. Open help via command

    1. Test case: Type `help` in the command box and press Enter.

    2. Expected: A help window or pane opens showing the list of supported commands and short usage examples.

2. Re-open / focus when already open

    1. Test case: With help open, type `help` again or press the help accelerator (F1).

    2. Expected: The help window/pane gains focus (no duplicate windows opened). If a separate window is used, it is brought to the front.

3. Accelerator / focus edge cases

    1. Test case: Press F1 while focus is in `CommandBox` or `ResultDisplay` (text input controls).

    2. Expected: Help opens or is focused despite text input controls consuming function keys (verify fallback event filter works).

4. Content correctness

    1. Test case: Inspect help content and verify the `find`, `filter`, `view`, and `help` entries match the documented usage and examples.

    2. Expected: Command words and sample usages are accurate and executable.

### Find command (`find`) — manual tests

1. Basic single-keyword search

    1. Test case: Enter `find Alice` where "Alice" exists in sample data.

    2. Expected: Displayed list shows students whose names contain a word starting with "Alice" (case-insensitive). Result count shown matches number of displayed rows.

2. Multiple-keyword search

    1. Test case: Enter `find Al Bob` where both keywords match different students.

    2. Expected: Displayed list contains students matching any of the keywords (OR across keywords). No duplicates; count is correct.

3. Case and prefix matching

    1. Test case: Enter `find ann` to match "Annabelle" and `find ANN`.

    2. Expected: Matching is case-insensitive and supports prefix matching as documented.

4. Empty or whitespace-only query

    1. Test case: Enter `find` with no keywords or only whitespace.

    2. Expected: Command rejected with a usage/error message; displayed list remains unchanged.

5. Illegal characters

    1. Test case: Enter `find @@@` or unusual punctuation.

    2. Expected: Behaviour consistent with documentation (either treated as literal keyword or rejected); error message clarifies allowed input if rejected.

6. Interaction with filters

    1. Test case: Apply a `filter` (e.g., `filter crs/CS2103T`), then `find Alice` where Alice is outside the filter.

    2. Expected: Document observed behaviour (whether `find` searches within filtered view or full dataset) and ensure it matches the Developer Guide's statement.

7. After mutations

    1. Test case: Add a student matching `find` keyword, then run `find`; delete a matching student and run `find` again.

    2. Expected: Results reflect current model state (add appears, deleted entries disappear).

### Filter command (`filter`) — manual tests

1. Single-criterion filters

    1. Test case: `filter crs/CS2103T` ; `filter tg/T01` ; `filter p/on_track` ; `filter abs/2`.

    2. Expected: Each command restricts the displayed list to students matching the given criterion; feedback shows the match count.

2. Multi-criterion filters (AND semantics)

    1. Test case: `filter crs/CS2103T tg/T01 p/on_track`.

    2. Expected: Displayed list contains only students satisfying all supplied criteria; removing a criterion expands the set.

3. Missing parameter values

    1. Test case: `filter crs/` or `filter abs/`

    2. Expected: Command rejected with a message indicating the missing value and correct usage. Displayed list unchanged.

4. Invalid formats

    1. Test case: `filter tg/@@@` or `filter abs/xyz` or `filter p/unsupported_status`.

    2. Expected: Rejected with a clear error message explaining valid formats/values.

5. Absence boundary checks

    1. Test case: `filter abs/0`, `filter abs/9999`, `filter abs/-1`.

    2. Expected: `abs/0` matches students with zero absences; very large numbers return empty set (or behave consistently); negative numbers are rejected.

6. No-match combinations

    1. Test case: Combine criteria that yield no results.

    2. Expected: Displayed list becomes empty and feedback indicates no matches.

7. Interaction with `list` and subsequent commands

    1. Test case: Apply a `filter`, then run `list`, then `filter` again.

    2. Expected: `list` resets the displayed view to all students; subsequent `filter` applies to the full dataset.

### Adding a student

1. Adding a student with valid fields

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Adding a student with missing required fields

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Adding a student with invalid field values

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

4. Adding a duplicate student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Editing a student

1. Editing a student with valid fields

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Editing a student with invalid fields

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Editing a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

4. Editing with missing edit fields

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Deleting a student

1. Deleting a student by index

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Deleting a student by student details

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Confirming a deletion

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

4. Cancelling a deletion

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

5. Deleting with invalid command format

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

6. Deleting a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Updating progress

1. Updating progress with a valid status

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Updating progress with an invalid status

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Updating progress for a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Marking attendance

1. Marking attendance with valid input

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Marking attendance with invalid input

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Marking attendance for a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Adding a remark

1. Adding a remark with valid input

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Adding a remark with invalid input

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Adding a remark to a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Viewing student details / remarks

1. Viewing a student with valid input

    1. Test case: Ensure a student is visible in the current displayed list, then enter `view 1` (or the appropriate index) and press Enter.

    2. Expected: The detail pane displays the selected student's full information (name, student ID, course, tutorial group, email, tele), attendance summary and remark entries. All fields render correctly; long text wraps or scrolls.

2. Viewing a non-existent / out-of-range index

    1. Test case: Enter `view 9999` (index greater than displayed list size) or `view 0` if 0 is invalid.

    2. Expected: Command rejected with an "invalid index" or usage error message; detail pane remains unchanged.

3. Viewing after filtering

    1. Test case: Apply a `filter` that changes the displayed list, then `view 1` to view the first item in the filtered list.

    2. Expected: `view` uses the current filtered ordering and index; the pane shows the selected student from the filtered view.

4. View and delete interaction

    1. Test case: `view 1` to show a student's details, then delete that student (`delete 1` + confirm).

    2. Expected: After deletion, the detail pane is cleared or replaced by a placeholder message indicating no student is selected; the UI remains stable and does not throw exceptions.

5. Repeated view commands

    1. Test case: Call `view 1` multiple times in succession.

    2. Expected: Behaviour is idempotent; repeated calls simply refresh the same details without error.

6. Remarks-heavy student

    1. Test case: View a student with many or long remarks.

    2. Expected: Remarks list scrolls if needed; entries show timestamps and content correctly; no layout overflow.

3. Re-opening the view when it is already open

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Listing students

1. Listing all students

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Clearing the student list / sample data

1. Clearing all students

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Clearing when the list is already empty

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Clearing filters

1. Clearing an active filter

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Clearing when no filter is active

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Saving data

1. Data persistence after normal usage

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Dealing with missing data files

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

3. Dealing with corrupted data files

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

### Suggested exploratory testing

1. Combining multiple commands in sequence

    1. _{Fill in workflow}_

2. Testing invalid inputs and edge cases

    1. _{Fill in workflow}_

3. Testing persistence across restarts

    1. _{Fill in workflow}_


## **Appendix: Planned Enhancements**

1.Relax student name and find command keywords validation to support special characters. Currently, the name field accepts only alphanumeric characters and spaces; we plan to extend this to support names containing hyphens, apostrophes, and other common punctuation, such as “O’Connor” and “Smith-Jones.”

2.Extend find to support prefix-based search across additional fields such as student ID, email, and course, instead of names only.

3.Add support for multi-value filtering. Currently, each filter prefix accepts only a single value; we plan to extend this to allow multiple values under the same prefix in a single filter command.

4.Add support for more flexible absence filtering. Currently, absence filtering only supports values greater than or equal to a given threshold; we plan to extend this to support exact values, upper bounds, and ranges.
