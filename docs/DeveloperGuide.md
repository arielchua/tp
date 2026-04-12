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
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface`) mentioned in the previous point.

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
* exposes functionality through the `Storage` interface, which extends both `AddressBookStorage` and `UserPrefsStorage`.
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
* delegates JSON conversion of TeachAssist data to classes such as `JsonSerializableAddressBook`, `JsonAdaptedPerson`, and `JsonAdaptedRemark`.
* uses concrete storage classes `JsonAddressBookStorage` and `JsonUserPrefsStorage`, which handle reading from and writing to files on disk.
* is implemented primarily by `StorageManager`, which coordinates `JsonAddressBookStorage` and `JsonUserPrefsStorage` to provide a unified persistence interface.

TeachAssist data stored by the `Storage` component includes not only persons, but also additional persisted fields such as cancelled weeks, weekly attendance data, progress, and remarks.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------
## **Key Feature Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Feature: Filter Command

#### Overview
The filter command allows Teaching Assistants (TAs) to narrow down the displayed student list using one or more criteria. This is especially useful for managing large cohorts and identifying specific groups, such as students who are at-risk or frequently absent.
The command only affects the current view and does not modify any underlying student data.
The supported criteria are:
* Course ID (crs/)
* Tutorial Group (tg/)
* Progress Status (p/)
* Minimum Absence Count (abs/)
The expected format is:
filter [crs/COURSE_ID] [tg/TUTORIAL_GROUP] [p/PROGRESS_STATUS] [abs/MIN_ABSENCE_COUNT]
Example:
filter crs/CS2103T tg/T01 abs/3
This displays students in CS2103T T01 with at least 3 absences.
#### Filtering behavior
Multiple criteria are combined using logical AND
A student is shown only if they satisfy all provided filters
If no criteria are provided, the command is considered invalid
Matching rules:
Course ID and Tutorial Group → case-insensitive exact match
Progress Status → exact enum match
Absences → threshold match (>=)

#### Implementation
The filter feature is implemented using:
FilterCommand
FilterCommandParser
FilterMatchesPredicate
**Execution flow:**
1. FilterCommandParser parses input arguments and validates prefixes
2. Ensures at least one filtering criterion is present
3. Constructs a FilterMatchesPredicate with the provided conditions
4. FilterCommand#execute(Model model) is invoked by the LogicManager
5. Command calls model.updateFilteredPersonList(predicate)
6. The model updates the filtered list
7. UI automatically refreshes via its binding to the observable list
8. A CommandResult is returned showing the number of matched students

Predicate design
FilterMatchesPredicate implements Predicate<Person> and stores each criterion as an optional field.
The test(Person person) method evaluates:
Course and tutorial group matching
Progress status equality
Absence count threshold
A student passes the predicate only if all present criteria evaluate to true.

Design Considerations
Aspect: Combining multiple criteria
Current choice: Logical AND
Pros: Predictable; supports narrowing down results effectively
Cons: Cannot perform OR-based queries (e.g., Course A or Course B)
Alternative: Logical OR
Pros: Enables broader searches
Cons: Less useful for refinement; may return overly large result sets
Aspect: Representation of filtering logic
Current choice: Single predicate with optional fields
Pros: Simple and centralized logic; easy to debug
Cons: May become bulky as more criteria are added
Alternative: Predicate composition (e.g., chaining smaller predicates)
Pros: More modular and flexible
Cons: Adds complexity; harder to trace combined filtering behavior


### Feature: Delete Student

#### Overview

The `delete` command removes a student from TeachAssist. Compared to the original AB3 implementation, TeachAssist extends this feature in two ways:

- It supports deleting a student either by displayed index or by exact student identity fields: `StudentId`, `CourseId`, and `TGroup`.
- It introduces a confirmation step before the deletion is carried out.

This is useful in TeachAssist because student records are identified by student-specific attributes rather than generic contact information, and accidental deletion of a student record should be avoided.

**Supported delete modes**

TeachAssist supports the following delete modes:

- **Delete by displayed index**  
  The user deletes a student using the index shown in the current displayed student list.  
  Example: `delete 1`

- **Delete by exact student details**  
  The user deletes a student by specifying the student’s `StudentId`, `CourseId`, and `TGroup`.  
  Example: `delete id/A1234567X crs/CS2103T tg/T01`

This identity-based deletion mode is useful when the user wants to target a specific student directly, rather than relying on the current displayed index.

#### Implementation

The `delete` feature is implemented using `AddressBookParser`, `DeleteCommandParser`, `DeleteCommand`, `ConfirmedDeleteCommand`, `ConfirmCommand`, `CancelCommand`, and `ConfirmationManager`.

`AddressBookParser` first checks whether TeachAssist is currently waiting for a confirmation response. If there is a pending delete action, `yes` is parsed into a `ConfirmCommand` and `no` is parsed into a `CancelCommand`. Otherwise, normal command parsing proceeds.

When the user enters a `delete` command, `AddressBookParser` delegates argument parsing to `DeleteCommandParser`. `DeleteCommandParser` supports two valid formats:

- `delete INDEX`
- `delete id/STUDENT_ID crs/COURSE_ID tg/TGROUP`

`DeleteCommandParser` performs format-level validation, including:
- checking that the input is not empty
- distinguishing between index-based and identity-based deletion
- ensuring that all required prefixes for detail-based deletion are present
- rejecting duplicate prefixes
- rejecting malformed index input
- rejecting unexpected trailing text after an index

After successful parsing, a `DeleteCommand` object is created. However, checks that depend on the current model state are deferred to command execution. For example, although the parser can verify that an index is written in a valid format, it cannot determine whether that index is within the bounds of the current filtered student list. Similarly, although the parser can verify that `StudentId`, `CourseId`, and `TGroup` are provided in the correct format, it cannot determine whether those values actually match a student in the current filtered list. These checks are therefore performed in `DeleteCommand#execute(Model)`.

The confirmation workflow proceeds as follows:

- `LogicManager#execute(String)` calls `AddressBookParser#parseCommand(String)`.
- `AddressBookParser` uses `DeleteCommandParser` to create a `DeleteCommand`.
- `DeleteCommand#execute(Model)` resolves the target student from the current filtered list.
- If the target student cannot be resolved, a `CommandException` is thrown and no confirmation is requested.
- If the target student is successfully resolved, `DeleteCommand` creates a `ConfirmedDeleteCommand` containing the resolved `Person`.
- `DeleteCommand` stores this pending command in `ConfirmationManager` using `ConfirmationManager#setPendingCommand(Command)`.
- `DeleteCommand` then returns a `CommandResult` containing the confirmation message.

When the user enters `yes`, `AddressBookParser` returns a `ConfirmCommand`. `ConfirmCommand#execute(Model)` retrieves the pending `ConfirmedDeleteCommand` from `ConfirmationManager`, clears the pending state, and executes it. `ConfirmedDeleteCommand#execute(Model)` then performs the actual deletion through `Model#deletePerson(Person)`.

When the user enters `no`, `AddressBookParser` returns a `CancelCommand`. `CancelCommand#execute(Model)` clears the pending command in `ConfirmationManager` and returns a cancellation message.

This separation keeps responsibilities clear:
- `DeleteCommand` resolves the deletion target and initiates confirmation.
- `ConfirmedDeleteCommand` performs the actual deletion.
- `ConfirmCommand` and `CancelCommand` handle the user’s confirmation response.
- `ConfirmationManager` stores the pending command between the initial delete request and the final user response.

<box type="info" seamless>

**Relevant diagram:** Delete-related class structure.

<puml src="diagrams/DeleteClassDiagram.puml" width="600" />

</box>

<box type="info" seamless>

**Relevant diagram:** Successful confirmation flow after the user enters `yes`.

<@isha to put sequence diagram here>

</box>

#### Design considerations

One possible design was to delete the student immediately after a valid `delete` command. This would have produced a simpler command flow, but it was rejected because it makes accidental deletion easier.

The chosen design introduces a confirmation step. This adds extra classes and command flow, but improves safety by requiring an explicit user confirmation before the student record is removed. This is more suitable for TeachAssist, where student data should not be deleted unintentionally.

### Feature: Update Progress

#### Overview

The `progress` command allows TAs to record a student's current academic or follow-up status in TeachAssist.

This helps TAs quickly identify which students are doing well, which students may need support, and which students require closer monitoring. By storing progress directly in each student record, TeachAssist makes it easier to keep track of follow-up priorities across multiple students.

#### Progress representation

TeachAssist represents progress using a `Progress` enum in the model.

The supported progress values are:
- `ON_TRACK`
- `NEEDS_ATTENTION`
- `AT_RISK`
- `NOT_SET`

`NOT_SET` is the default value and represents the absence of an explicitly assigned progress status.

Using an enum ensures that only valid progress values can be stored, which simplifies validation and prevents inconsistent states.

#### Implementation

The `progress` feature is implemented using `ProgressCommand`, `ProgressCommandParser`, the `Progress` enum, and the model's person update mechanism.

When the user enters a `progress` command, `AddressBookParser` delegates parsing of the command arguments to `ProgressCommandParser`. `ProgressCommandParser` parses the target student index and the new progress value. The parser validates that the index is present and in a valid format, that the `p/` prefix is provided, and that the progress value is one of the supported enum values. After successful parsing, a `ProgressCommand` is created.

During execution, `ProgressCommand` retrieves the student at the specified index from the current filtered student list. If the index is out of range, the command fails. If the index is valid, a new `Person` object is created based on the original student but with the updated progress value. The old student record is then replaced with the updated one through the model.

This follows the same general approach as other commands that modify a student record: instead of mutating the existing student directly, TeachAssist creates an updated `Person` object and replaces the original in the model. If the new progress value is `NOT_SET`, the student's progress is effectively cleared.

<box type="info" seamless>

**Note:** The sequence diagram illustrating how `ProgressCommand` is executed is shown in the [Logic component](#logic-component) section above.

</box>

#### UI integration

Progress is displayed on each student card in the UI as a progress label. The label is colour-coded to help TAs quickly distinguish student status across `ON_TRACK`, `NEEDS_ATTENTION`, and `AT_RISK`. If the progress value is `NOT_SET`, no progress label is shown. This design keeps the UI uncluttered while still surfacing important student status information when it is available.

#### Design considerations

A key design consideration was how to represent progress in the model. One possible approach was to store progress as a plain string. However, this would require repeated string validation and would make invalid values easier to introduce. The chosen approach was to represent progress using an enum, which ensures that only valid progress states can exist and makes the implementation easier to maintain.

Another design consideration was whether to show `NOT_SET` explicitly in the UI. Showing `NOT_SET` as a visible label would make the implementation more uniform, but it would also add unnecessary visual clutter for students whose progress has not been set. The chosen design hides `NOT_SET` in the UI, so only meaningful progress statuses are shown.

### Feature: Mark Attendance

#### Overview

The `markattendance` command allows tutors to record or update a student’s attendance for a specific week. This feature enables per-week attendance tracking instead of aggregate counts, providing finer control over tutorial participation records.

Each student’s attendance is tracked weekly, allowing tutors to:
- Mark attendance as **attended (Y)**, **absent (A)**, or **not marked (N)**
- Prevent marking for cancelled weeks
- Avoid duplicate updates to the same attendance status
- Automatically respect course-level cancelled weeks

---

#### Attendance representation

Attendance is represented as a **structured list of weekly records**.

- Each `Person` contains a `WeekList`
- `WeekList` stores a fixed-size array of **13 `Week` objects**
- Each `Week` represents one tutorial session

Each `Week` contains:
- `weekNo` — week number
- `status` — current attendance state
- `prevStatus` — used for cancellation recovery

Attendance status is defined using an enum:
- `Y` → Attended
- `A` → Absent
- `N` → Not marked
- `C` → Cancelled

Cancelled weeks are derived from a **course–tutorial level cancelled week map** stored in the `Model`. When a week is cancelled, all students in the same `(CourseId, TGroup)` pair automatically inherit the cancelled state.

---

#### Implementation

The `markattendance` feature is implemented using `MarkAttendanceCommand` and `MarkAttendanceCommandParser`.

**Parsing phase:**
1. User input is tokenized using `ArgumentTokenizer`
2. Required prefixes (`week/`, `sta/`) are validated
3. Values are parsed into:
    - `Index` (student)
    - `Index` (week)
    - `Week.Status` (attendance state)

**Execution phase:**
1. The command retrieves the filtered student list from the `Model`
2. The target `Person` is identified using the provided index
3. The student’s `WeekList` is copied to preserve immutability
4. The specified week is validated:
    - Must be within range (1–13)
    - Must not be cancelled
5. The attendance status is updated via `WeekList`
6. A new `Person` object is created with the updated `WeekList`
7. The model replaces the old `Person` with the updated one

If an invalid operation occurs (e.g., duplicate status or cancelled week), a `CommandException` is thrown.

---

#### Sequence diagram

The following diagram shows how attendance input is parsed, validated, and applied to the target student.
<puml src="diagrams/MarkAttendanceSequenceDiagram.puml" width="600" />


### Feature: Cancel Week

#### Overview

The `cancelweek` command allows tutors to mark a specific tutorial week as cancelled for **all students belonging to a given course and tutorial group**. This is useful when a tutorial session is cancelled due to public holidays, instructor absence, or rescheduled lessons.

The command word is `cancelweek`, and its expected format is:

`cancelweek crs/COURSE_ID tg/TUTORIAL_GROUP week/WEEK_NUMBER`

For example, `cancelweek crs/CS2103T tg/T01 week/5` cancels week 5 for all students in CS2103T T01.

---

#### Cancelled week representation

Cancelled weeks are stored at **course-tutorial level**, not per student.

The `Model` maintains:
- A mapping of `(CourseId, TGroup)` pairs
- Each entry stores cancelled week indices

When a week is cancelled:
- The week index is stored in the model
- All students in the matching `(CourseId, TGroup)` are updated
- Their `WeekList` marks the week as `C`
- Previous status is stored for recovery
  - however, when TeachAssist is closed you cannot recover the statuses, in cancelled week

---

#### Implementation

The `cancelweek` feature is implemented using `CancelWeekCommand` and `CancelWeekCommandParser`.

Execution flow:
1. Parser validates `crs/`, `tg/`, and `week/`
2. Command checks that `(CourseId, TGroup)` exists
3. Command validates week range
4. Model checks duplicate cancellation
5. `ModelManager.addCancelledWeek()` is called
6. Matching students' `WeekList` objects are updated
7. Cancelled week is stored in persistent map

If the week is already cancelled, a `CommandException` is thrown.

### Feature: Uncancel Week

#### Overview

The `uncancelweek` command removes a previously cancelled week for a specific course and tutorial group. This restores the week’s attendance status for all affected students.

The command word is `uncancelweek`, and its expected format is:

`uncancelweek crs/COURSE_ID tg/TUTORIAL_GROUP week/WEEK_NUMBER`

For example, `uncancelweek crs/CS2103T tg/T01 week/5` restores week 5.

---

#### Behaviour

When a cancelled week is restored:
- The week is removed from the cancelled week map
- All students in the `(CourseId, TGroup)` pair are updated
- Their `WeekList` restores the previous status
- UI reflects restored attendance values

---

#### Implementation

The `uncancelweek` feature is implemented using `UnCancelWeekCommand` and `UnCancelWeekCommandParser`.

Execution flow:
1. Parser validates prefixes
2. Command checks course–tutorial existence
3. Command verifies week is currently cancelled
4. `ModelManager.removeCancelledWeek()` is called
5. Matching students are updated
6. Persistent map is updated

If the week is not cancelled, the command safely does nothing.


### Feature: Remark Command
#### Overview

The `remark` command allows a teaching assistant to attach a short note to a specific student. This is useful for recording contextual observations that are not captured by the standard student fields (e.g class participation, submission behaviour, consultation follow-ups, or other teaching-related comments)

The command targets a student by their displayed student index in the current person list and adds a remark to that student. The remark is intended to help TAs keep track of student-specific context across multiple interactions. The command word is `remark`, and its expected format is:

`remark INDEX txt/REMARK`

For example, `remark 1 txt/Participates actively in class` adds a new remark `Participates actively in class` to the first student in the displayed list.

#### Remark representation

In the model, a remark is represented as a dedicated `Remark` object rather than plain text alone. Each remark stores:
- the remark text
- the date on which the remark was created

This design allows each remark to carry basic metadata in addition to its content. In the current implementation, the creation date is automatically assigned using `LocalDate.now()` when the command is parsed. This means the user only provides the text of the remark, while the system records the date implicitly.

- Remarks are associated with a `Person` as a list of remark objects. 
- In storage, `JsonAdaptedPerson` stores `remarks` as a `List<JsonAdaptedRemark>`.
- Each `JsonAdaptedRemark` contains a `text` field and a `date` field.
- During deserialization, each adapted remark is converted back into a model-level `Remark` object and reattached to the corresponding person.

#### Implementation

The `remark` feature is implemented using the `RemarkCommand` and `RemarkCommandParser` classes. The parser is responsible for extracting the target student index and the remark text from user input. It tokenizes the input using the `txt/` prefix, validates that both the preamble and the remark body are present, parses the preamble as an `Index`, trims the remark text, and constructs a new `Remark` object with the supplied text and the current date. It then returns a `RemarkCommand` containing the parsed index and newly created remark.

When `RemarkCommand#execute` is called, the command first retrieves the currently filtered person list from the model. It checks whether the provided index is within bounds; if not, it throws a `CommandException` using `Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX`. Otherwise, it retrieves the target `Person` from the displayed list and adds the new remark to that person using `personToEdit.addRemark(remark)`. A success message is then returned to the user.

An important implementation detail is that the current version modifies the retrieved `Person` object directly by invoking `addRemark` on it. In other words, the command does not construct a replacement `Person` and does not call a model-level replacement method such as `setPerson(...)`. In the current implementation, the command adds the new `Remark` directly to the selected `Person` object using `addRemark(remark)`.

The parsing and execution flow can therefore be summarized as follows:
1. User enters a `remark` command with a student index and remark text.
2. `RemarkCommandParser` validates the format and creates a `Remark` with the current date.
3. A `RemarkCommand` is created with the parsed index and remark.
4. `RemarkCommand#execute` checks that the student index is valid.
5. The target student's remark list is updated by adding the new remark.
6. A success result is returned.

#### Design Considerations

Using a dedicated `Remark` object instead of a raw string makes the feature more extensible. Since each remark already stores both text and date, the design can be extended in future to support richer metadata such as author, category, or edit history without changing the overall remark-management structure. The storage layer already supports this object-based design through `JsonAdaptedRemark`.

### Feature: Unremark Command

#### Overview

The `unremark` command allows a teaching assistant to delete an existing remark from a specific student record. This is useful when a remark is no longer relevant, was added by mistake, or needs to be removed to keep the student’s record concise and up to date.

The command targets a student by their displayed index in the current person list, and then targets a specific remark belonging to that student by its remark index. The command word is `unremark`, and its expected format is:

`unremark INDEX r/REMARK_INDEX`

For example, `unremark 1 r/2` removes the second remark from the first student in the displayed list.

#### Unremark Representation

The `unremark` command operates on the same `Remark` representation described in the `remark` feature. Each `Person` maintains a list of `Remark` objects, and `unremark` removes one existing remark from that list based on its remark index.

#### Implementation

The `unremark` feature is implemented using the `UnremarkCommand` and `UnremarkCommandParser` classes. The parser tokenizes user input using the `r/` prefix, validates that both the student index and the remark index are present, parses both values as `Index` objects, and constructs an `UnremarkCommand` containing the parsed student index and remark index.

When `UnremarkCommand#execute` is called, the command first retrieves the currently filtered person list from the model. It checks whether the provided student index is within bounds; if not, it throws a `CommandException` using `Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX`. It then retrieves the target `Person` from the displayed list and checks whether the provided remark index is valid for that person’s remark list. If the remark index is out of bounds, the command throws a `CommandException` with `MESSAGE_INVALID_REMARK_INDEX`. Otherwise, the command deletes the selected remark by calling `personToEdit.deleteRemark(remarkIndex)`, and returns a success message.

The execution flow can therefore be summarized as follows:
1. User enters an `unremark` command with a student index and a remark index.
2. `UnremarkCommandParser` validates the format and parses both indices.
3. An `UnremarkCommand` is created with the parsed student index and remark index.
4. `UnremarkCommand#execute` checks that the student index is valid.
5. The command checks that the remark index is valid for the selected student.
6. The specified remark is removed from the student’s remark list.
7. A success result is returned.

#### Design Considerations

Using a separate `unremark` command allows remark deletion to remain explicit and precise. Since a student may have multiple remarks, requiring both the student index and the remark index ensures that the command targets exactly one stored remark. This avoids ambiguity and keeps remark management consistent with the object-based remark representation used in the model and storage layers.

### Feature: View Command

#### Overview

The `view` command opens a dedicated panel on the right-hand side of the UI to display the full details of a single student. This provides a comprehensive, at-a-glance summary, including the student's attendance record and all associated remarks, which are not fully visible in the main list.

#### Implementation

The `view` command is implemented by `ViewCommand`, `ViewCommandParser`, and the `ViewWindow` UI component.

1.  **`ViewCommandParser`**: Parses the user input to extract the target student's index from the currently displayed list.
2.  **`ViewCommand`**: Retrieves the `Person` object from the `Model` at the specified index. It then returns a `CommandResult` with the `showView` flag set to `true` and a reference to the `Person` object to be viewed.
3.  **`MainWindow`**: Receives the `CommandResult` and calls `handleView(person)`. This method updates the `ViewWindow` with the student's data and displays it in the `viewWindowPlaceholder`.
4.  **`ViewWindow`**: A `UiPart` that contains FXML elements for displaying person details. Its `setPerson(Person person)` method populates the UI fields with the student's information, including dynamically generated cards for each remark.

#### View Window Auto-Sync

The `ViewWindow` has logic to automatically update or clear itself after any command is executed. This ensures the displayed details do not become stale. For example, if the user edits a student who is currently being viewed, the view should refresh. If the student is deleted or filtered out of the main list, the view should be cleared.

The activity diagram below illustrates this logic, which is primarily handled by the `updateViewWindowAfterCommand()` method in `MainWindow`.

<puml src="diagrams/ViewWindowSyncActivityDiagram.puml" alt="View Window Auto-Sync Logic" />


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

TeachAssist is a standalone, local CLI application designed to streamline student data management for university teaching assistants through a high-speed, typing-based interface. The system focuses exclusively on the structured organization of student information and manual consultation logging, deliberately eschewing integration with external platforms like Canvas or cloud-based databases. To maintain its lightweight and specialized nature, TeachAssist does not include features for automated communication, calendar scheduling, or academic analytics; it does not compute grades, generate transcripts, or provide predictive student tracking. By prioritizing a command-line workflow over a traditional GUI, TeachAssist provides an efficient, distraction-free environment for TAs to manage high volumes of contacts and administrative records across multiple classes.

**Target user profile**:

TeachAssist is specifically optimized for a distinct niche of academic administrators. Our ideal user:
* Role: Full-time University Teaching Assistants (TAs) at NUS.
* Workload: Manages a significant volume of student contacts and consultation records across multiple classes and tutorial groups each semester.
* Platform Preference: Prefers a dedicated desktop environment over web-based tools.
* Technical Proficiency: Reasonably comfortable with Command Line Interface (CLI) applications and values the precision of text-based input.
* Performance: A fast typist who finds traditional Mouse/GUI interactions cumbersome and "slow" for repetitive data entry.

**Value proposition**: 
TeachAssist provides a high-speed, structured student management system that bridges the gap between disorganized spreadsheets and overly complex enterprise software. By utilizing a typing-based interface, it allows TAs to execute administrative tasks—such as updating student details or logging consultation notes—significantly faster than a typical mouse-driven application.


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

1. User enters a `filter` command with one or more criteria (e.g., `crs/`, `tg/`, `p/`, `abs/`).
2. TeachAssist updates the displayed student list to show only students matching all provided criteria.
3. TeachAssist displays feedback summarising the active filter and the number of matching students.
4. Use case ends.

**Extensions:**

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

<box type="info" seamless></box>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box></box>

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

5. Entering another command while deletion is pending

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter another command such as `list`

    2. **Expected behaviour:** The pending deletion is cleared and the new command is processed normally. No student is deleted unless the user re-enters the delete command and confirms it.

6. Deleting with invalid command format

    1. **Test case:** `delete abc`

    2. **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown.

7. Deleting with invalid index format

    1. **Test case:** `delete -1`

    2. **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown to indicate that the index is invalid.

8. Deleting a non-existent student by details

    1. **Prerequisite:** Ensure that no student in the currently displayed list matches these 3 fields: `id/A0000000Z crs/CS9999 tg/T99`

    2. **Test case:** `delete id/A0000000Z crs/CS9999 tg/T99`

    3. **Expected behaviour:** The command is rejected because no matching student exists in the current filtered list, no confirmation is requested, and an error message is shown.

### Updating progress

1. Updating progress with a valid status

    1. **Test case:** `progress 1 p/ON_TRACK`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list and the progress status is valid, the student’s progress is updated to `ON_TRACK` and a success message is shown.

2. Updating progress with an invalid status

    1. **Test case:** `progress 1 p/GOOD`

    2. **Expected behaviour:** The command is rejected, no student record is updated, and an error message is shown to indicate that the progress status is invalid.

3. Updating progress with an invalid index

    1. **Test case:** `progress 999 p/AT_RISK`

    2. **Expected behaviour:** If index `999` is outside the bounds of the current filtered list, the command is rejected, no student record is updated, and an error message is shown.

4. Removing progress using `NOT_SET`

    1. **Test case:** `progress 1 p/NOT_SET`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student’s progress is updated to `NOT_SET`. The progress tag is removed from the student card in the UI, and a success message is shown.

5. Updating progress with invalid command format

    1. **Test case:** `progress p/ON_TRACK`

    2. **Expected behaviour:** The command is rejected because the required student index is missing, no student record is updated, and an error message is shown.
### Marking attendance

1. Marking attendance with valid input

   1. Test case: `markattendance 1 week/3 sta/Y`

   2. Expected: Student at index 1 has week 3 marked as attended. Success message displayed.

2. Marking attendance with invalid week

   1. Test case: `markattendance 1 week/20 sta/Y`

   2. Expected: Command rejected with invalid week message.

3. Marking attendance with cancelled week

   1. Test case:
      `cancelweek crs/CS2103T tg/T01 week/3`
      `markattendance 1 week/3 sta/Y`

   2. Expected: Command rejected. Week is cancelled.

4. Marking attendance duplicate status

   1. Test case:
      `markattendance 1 week/2 sta/Y`
      `markattendance 1 week/2 sta/Y`

   2. Expected: Second command rejected.

5. Marking attendance for non-existent student

   1. Test case: `markattendance 999 week/2 sta/Y`

   2. Expected: Invalid index error.

### Cancelling a week

1. Cancelling a week with valid input

   1. Test case: `cancelweek crs/CS2103T tg/T01 week/5`

   2. Expected: Week 5 marked cancelled for all students.

2. Cancelling already cancelled week

   1. Test case: Run command twice

   2. Expected: Second command rejected.

3. Cancelling invalid week

   1. Test case: `cancelweek crs/CS2103T tg/T01 week/20`

   2. Expected: Invalid week error.

4. Cancelling non-existent course/tutorial

   1. Test case: `cancelweek crs/CS9999 tg/T99 week/2`

   2. Expected: Course/tutorial not found.

### Uncancelling a week

1. Uncancelling valid week

   1. Test case:
      `cancelweek crs/CS2103T tg/T01 week/4`
      `uncancelweek crs/CS2103T tg/T01 week/4`

   2. Expected: Week restored for all students.

2. Uncancelling non-cancelled week

   1. Test case: `uncancelweek crs/CS2103T tg/T01 week/3`

   2. Expected: No change.

3. Uncancelling invalid week

   1. Test case: `uncancelweek crs/CS2103T tg/T01 week/20`

   2. Expected: Invalid week error.

4. Uncancelling non-existent course/tutorial

   1. Test case: `uncancelweek crs/CS9999 tg/T99 week/1`
   2. Expected: Course/tutorial not found.

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

1.Relax student name and find command keywords validation to support special characters. Currently, the name field accepts only alphabetical characters and spaces; we plan to extend this to support names containing hyphens, apostrophes, and other common punctuation, such as “O’Connor” and “Smith-Jones.”

2.Extend find to support prefix-based search across additional fields such as student ID, email, and course, instead of names only.

3.Add support for multi-value filtering. Currently, each filter prefix accepts only a single value; we plan to extend this to allow multiple values under the same prefix in a single filter command.

4.Add support for more flexible absence filtering. Currently, absence filtering only supports values greater than or equal to a given threshold; we plan to extend this to support exact values, upper bounds, and ranges.

5.Preserve and refresh embedded ViewWindow on edits. Currently, editing a person while their details are shown in the embedded right-hand `ViewWindow` causes the view to be cleared. This breaks UX by losing context. Planned change: when the edited person is the one currently displayed, keep the `ViewWindow` open and update its contents in-place to reflect the edited data. Only clear the view when the person is deleted or removed from the filtered list.

6.When parsing remarks, only the first `txt/` prefix is treated as the remark delimiter; subsequent `txt/` substrings are treated as part of the remark text.
