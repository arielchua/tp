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

The `delete` command removes a student from TeachAssist. Unlike the original AB3 implementation, which only supported deletion by displayed index and performed the deletion immediately, TeachAssist extends this feature in two ways.

First, TeachAssist supports deleting a student either by displayed index or by exact student details. This gives users flexibility when working with filtered lists or when they prefer to identify a student directly by their stored attributes.

Second, TeachAssist introduces a confirmation workflow before the deletion is carried out. This reduces the likelihood of accidental deletion and makes the feature safer to use, especially in a teaching context where student records are important and should not be removed unintentionally.

#### Supported delete modes

TeachAssist supports two modes of deletion.

The first mode is **deletion by displayed index**, where the user deletes a student using the index shown in the current filtered student list.

Example: `delete 1`

The second mode is **deletion by exact student details**, where the user deletes a student by specifying the student’s:
- `StudentId`
- `CourseId`
- `TGroup`

Example: `delete id/A1234567X crs/CS2103T tg/T01`

This identity-based deletion mode is useful when the user wants to target a specific student directly, rather than relying on the current displayed index.

#### Confirmation workflow

A valid `delete` command does not immediately remove the student from TeachAssist. Instead, the system first resolves the student targeted by the command and then prompts the user for confirmation.

For index-based deletion, TeachAssist first checks whether the given index is valid in the current filtered student list. For detail-based deletion, TeachAssist searches the currently displayed list for a student whose `StudentId`, `CourseId`, and `TGroup` all match the given values exactly.

If the student cannot be resolved, the command fails and no confirmation is requested. If the student is successfully resolved, TeachAssist creates a pending delete action and returns a confirmation prompt asking the user whether the deletion should proceed.

If the user enters `yes`, the pending deletion is executed and the student is removed from the model. If the user enters `no`, the pending deletion is discarded and no changes are made.

<box type="info" seamless>

**Relevant diagram:** Activity diagram showing the branching delete flow, including deletion by index, deletion by student details, and confirmation handling.

<@Isha place the activity diagram here!>

</box>

#### Implementation

The `delete` feature is implemented using `AddressBookParser`, `DeleteCommandParser`, `DeleteCommand`, `ConfirmedDeleteCommand`, `ConfirmCommand`, `CancelCommand`, and `ConfirmationManager`.

At the parsing stage, `AddressBookParser` first determines whether the system is currently waiting for a confirmation response. If a deletion is pending, it interprets `yes` as a `ConfirmCommand` and `no` as a `CancelCommand`. Otherwise, normal command parsing proceeds.

When the user enters a `delete` command, `AddressBookParser` delegates parsing of the command arguments to `DeleteCommandParser`. `DeleteCommandParser` supports two valid formats:
- `delete INDEX`
- `delete id/STUDENT_ID crs/COURSE_ID tg/TGROUP`

It performs format-level validation, such as:
- checking that the delete input is not empty
- distinguishing between index-based and identity-based deletion
- ensuring that all required prefixes for detail-based deletion are present
- rejecting duplicate prefixes
- rejecting malformed index input or unexpected trailing text after an index

After successful parsing, a `DeleteCommand` object is created. However, some validation is intentionally deferred to command execution. In particular, whether the target index is within the bounds of the current filtered list, and whether the given student details actually match a student in the current filtered list, can only be determined during execution because they depend on the current model state.

During execution, `DeleteCommand` resolves the student to be deleted. Once the student is identified, it does not delete the student immediately. Instead, it creates a `ConfirmedDeleteCommand` containing the resolved `Person` object and stores it in `ConfirmationManager` as the pending command. A confirmation message is then returned to the user.

If the user subsequently enters `yes`, `ConfirmCommand` retrieves the pending `ConfirmedDeleteCommand` from `ConfirmationManager`, clears the pending state, and executes it. `ConfirmedDeleteCommand` then performs the actual deletion by calling `Model#deletePerson(...)`.

If the user enters `no`, `CancelCommand` clears the pending command in `ConfirmationManager` and returns a cancellation message instead.

This separation keeps the responsibilities of the classes clear. `DeleteCommand` is responsible for resolving the deletion target and initiating confirmation, `ConfirmedDeleteCommand` performs the actual deletion, and `ConfirmationManager` stores the pending action between the initial delete request and the final user response.

<box type="info" seamless>

**Relevant diagram:** Delete-related class structure.

<puml src="diagrams/DeleteClassDiagram.puml" width="700" />

</box>

<box type="info" seamless>

**Relevant diagram:** Successful confirmation flow after the user enters `yes`.

<puml src="diagrams/ConfirmedDeleteSequenceDiagram.puml" width="700" />

</box>

#### Design considerations

A key design consideration was where to place the confirmation logic. An earlier approach placed delete-specific confirmation handling in the UI layer and in `LogicManager`. However, this weakened the separation of concerns because UI classes and top-level logic classes became aware of command-specific workflow details.

The final design keeps the confirmation workflow within the logic and command layers. `AddressBookParser` remains responsible for translating user input into command objects, while command execution and model mutation remain outside the parser. This preserves a cleaner abstraction boundary compared to handling delete confirmation directly in the UI.

Another design consideration was whether to delete the student immediately after parsing a valid command. This was rejected because it would make accidental deletions easier. The confirmation-based design was chosen to improve safety and usability.

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

**Use Case: UC01 - Purge Sample Data**<br>
**Actor:** User<br>
**MSS:**
1. User enters the purge command.
2. TeachAssist detects sample data present.
3. TeachAssist asks for confirmation.
4. User confirms the purge.
5. TeachAssist deletes all sample records.
6. TeachAssist confirms that sample data has been removed.
7. Use case ends.

**Extensions:**

* 3a. User cancels the purge.
    * 3a1. TeachAssist aborts the purge operation.
    * Use case ends.

**Use Case: UC02 – Add Student**<br>
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

**Use Case: UC05 – Mark Attendance**<br>
**Actor:** User<br>
**MSS:**

1. User selects a tutorial session.
2. TeachAssist displays the list of students in the tutorial group.
3. User marks each student as present or absent.
4. TeachAssist records the attendance for the session.
5. TeachAssist confirms the attendance record.
6. Use case ends.

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

**Use Case: UC07 – Record Participation**<br>
**Actor:** User<br>
**MSS:**

1. User selects a student.
2. User enters the participation recording command.
3. TeachAssist requests participation details.
4. User enters the participation score or description.
5. TeachAssist records the participation entry.
6. TeachAssist confirms the update.
7. Use case ends

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

**Use Case: UC09 – View Student History**<br>
**Actor:** User<br>
**MSS:**

1. User selects a student.
2. User requests to view the student’s history.
3. TeachAssist retrieves the student’s notes (UC06), attendance (UC5), and participation records (UC07), progress status (UC08).
4. TeachAssist displays the historical information.
5. User reviews the data.
6. Use case ends

**Extensions**

* 5a. User deletes a particular student history
    * 5a1. User selects a student to delete.
    * 5a2. User enters the delete command.
    * 5a3. TeachAssist requests confirmation.
    * 5a4. User confirms the deletion.
    * 5a5. TeachAssist removes the student record.
    * 5a6. TeachAssist displays confirmation.
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

**Use Case: UC13 – Clear Student Filters** <br>
**Actor:** User <br>
**MSS:**

1.User enters the command to clear the current filter.
2. TeachAssist removes the applied filtering criteria.
3. TeachAssist displays the full student list.
4. Use case ends.

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

### Viewing help

1. Opening the help window

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Re-opening help when it is already open

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

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

    1. **Test case:** `delete 1`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message asking the user to type `yes` to confirm or `no` to cancel.

2. Deleting a student by student details

    1. **Test case:** `delete id/A1234567X crs/CS2103T tg/T01`

    2. **Expected behaviour:** If a student matching the given `StudentId`, `CourseId`, and `TGroup` exists in the current filtered list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message asking the user to type `yes` to confirm or `no` to cancel.

3. Confirming a deletion

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter `yes`

    2. **Expected behaviour:** The pending deletion is executed, the student is removed from TeachAssist, and a success message is shown.

4. Cancelling a deletion

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter `no`

    2. **Expected behaviour:** The pending deletion is cancelled, no student is removed, and a cancellation message is shown.

5. Deleting with invalid command format

    1. **Test case:** `delete abc`

    2. **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown to indicate that the command format is invalid.

6. Deleting a non-existent student

    1. **Prerequisite:** ensure that no student in the currently displayed list matches these 3 fields: `id/A0000000Z crs/CS9999 tg/T99`

    2. **Test case:** `delete id/A0000000Z crs/CS9999 tg/T99`

    3. **Expected behaviour:** The command is rejected because no matching student exists in the current filtered list, no confirmation is requested, and an error message is shown.

### Updating progress

1. Updating progress with a valid status

    1. **Test case:** `progress 1 p/ON_TRACK`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list and the progress status is valid, the student’s progress is updated to `ON_TRACK` and a success message is shown.

2. Updating progress with an invalid status

    1. **Test case:** `progress 1 p/GOOD`

    2. **Expected behaviour:** The command is rejected, no student record is updated, and an error message is shown to indicate that the progress status is invalid.

3. Updating progress for a non-existent student

    1. **Prerequisite:** ensure that no student in the currently displayed list matches these 3 fields: `id/A0000000Z crs/CS9999 tg/T99`

    2. **Test case:** `progress id/A0000000Z crs/CS9999 tg/T99 p/AT_RISK`

    3. **Expected behaviour:** The command is rejected because no matching student exists in the current filtered list, no student record is updated, and an error message is shown.

4. Removing progress using `NOT_SET`

    1. **Test case:** `progress 1 p/NOT_SET`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student’s progress is updated to `NOT_SET`. The progress tag is removed from the student card in the UI, and a success message is shown.

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

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

2. Viewing a non-existent student

    1. _{Fill in test case}_

    2. _{Fill in expected behaviour}_

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
