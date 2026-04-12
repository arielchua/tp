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

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("edit 1 n/John")` API call as an example.

<puml src="diagrams/EditSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `edit 1 n/John` Command" />

<box type="info" seamless>

**Note:** The lifeline for `EditCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `EditCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `EditCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to edit a person's details).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `EditCommandParser`) which tokenizes the input using `ArgumentTokenizer` (with prefixes defined in CliSyntax) into `ArgumentMultimap`. `ParserValidators` then performs structural checks on the tokenized arguments (presence of required prefixed, rejecting unknown prefixes, and flagging blank values) and produces error messages via `ParserMessages`. `ParserUtil` is then used to validate and extract the individual field values, creating a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `EditCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

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
The `filter` command allows TAs to narrow down the displayed student list using one or more criteria. This is useful for managing large cohorts and identifying specific groups, such as students who are at-risk or frequently absent. The command only affects the current view and does not modify any underlying student data.

The supported criteria are:
* Course ID (`crs/`)
* Tutorial Group (`tg/`)
* Progress Status (`p/`)
* Minimum Absence Count (`abs/`)

Multiple criteria are combined using logical AND — a student is shown only if they satisfy all provided filters. If no criteria are provided, the command is rejected.

#### Implementation

The filter feature introduces one key class beyond the standard parser–command pattern: `FilterMatchesPredicate`.

`FilterMatchesPredicate` implements `Predicate<Person>` and stores each filtering criterion as an optional field. Its `test(Person)` method evaluates all present criteria against the given student:
* Course ID and Tutorial Group use case-insensitive exact matching.
* Progress Status uses exact enum matching.
* Absence Count uses threshold matching (≥).

A student passes the predicate only if all present criteria evaluate to true. During execution, `FilterCommand` passes this predicate to `Model#updateFilteredPersonList(Predicate)`, which updates the observable list that the UI is bound to.

#### Design considerations

**Aspect: Combining multiple criteria**

* **Current choice — Logical AND:** Predictable behaviour; supports narrowing down results effectively. However, it does not support OR-based queries (e.g., Course A or Course B).
* **Alternative — Logical OR:** Enables broader searches, but is less useful for refinement and may return overly large result sets.

**Aspect: Representation of filtering logic**

* **Current choice — Single predicate with optional fields:** Simple and centralised logic; easy to debug. However, it may become bulky as more criteria are added.
* **Alternative — Predicate composition (chaining smaller predicates):** More modular and flexible, but adds complexity and makes combined filtering behaviour harder to trace.


### Feature: Delete Student

#### Overview

The `delete` command removes a student from TeachAssist.

Compared to the original AB3 implementation, TeachAssist extends this feature in two ways:

- It supports deleting a student either by displayed index or by exact student identity fields: `StudentId`, `CourseId`, and `TGroup`.
- It introduces a confirmation step before the deletion is carried out.

This is useful because student records are important and should not be removed accidentally, especially when a TA may be managing many students across multiple courses and tutorial groups.

#### Supported delete modes

TeachAssist supports the following delete modes:

- **Delete by displayed index**
  The user deletes a student using the index shown in the current displayed student list.
  Example: `delete 1`

- **Delete by exact student details**
  The user deletes a student by specifying the student’s `StudentId`, `CourseId`, and `TGroup`.
  Example: `delete id/A1234567X crs/CS2103T tg/T01`

For detail-based deletion, the match is performed against the **entire TeachAssist list**, rather than only the currently filtered list. This allows the user to delete a specific student directly even if that student is not currently visible in the displayed list.

#### Implementation

The `delete` feature is implemented primarily using `DeleteCommand`, `ConfirmedDeleteCommand`, and `LogicManager`.

<box type="info" seamless>

**Relevant diagram:** Delete-related class structure.

<puml src="diagrams/DeleteClassDiagram.puml" width="600" />

</box>

`DeleteCommandParser` only performs brief format-level validation. It determines whether the input is index-based or detail-based, checks that the required fields are present, and constructs a `DeleteCommand`. The main design focus of this feature is the confirmation workflow rather than the parsing behaviour.

When the user enters a `delete` command, the command is first parsed into a `DeleteCommand`. However, the student is **not deleted immediately**.

Instead, `LogicManager` handles deletion as a two-stage workflow:

1. the target student is first resolved by `DeleteCommand`
2. the actual deletion only happens after the user explicitly confirms with `yes`

More specifically, after `DeleteCommand` is parsed, `LogicManager` detects that the parsed command is a `DeleteCommand`. It then calls `DeleteCommand#getConfirmedCommand(model)` to obtain a `ConfirmedDeleteCommand` for the intended student. At the same time, it calls `DeleteCommand#getConfirmationMessage(model)` to obtain the confirmation message to show to the user.

The resulting `ConfirmedDeleteCommand` is then stored temporarily inside `LogicManager` as a pending confirmation command.

This means that after a valid `delete` command is entered:

- no deletion has happened yet
- TeachAssist is waiting for the user’s next input
- the pending deletion is represented by a stored `ConfirmedDeleteCommand`

If the user enters `yes`, `LogicManager` executes the stored `ConfirmedDeleteCommand`. `ConfirmedDeleteCommand#execute(Model)` then performs the actual deletion through `Model#deletePerson(Person)`.

If the user enters `no`, `LogicManager` clears the stored pending command and no deletion occurs.

If the user enters some other command instead, `LogicManager` also clears the pending command. In other words, the confirmation is only valid for the immediate follow-up response, and any other input cancels the pending deletion flow.

This design separates the delete process into two distinct responsibilities:

- `DeleteCommand` identifies which student the user intends to delete and prepares the confirmation step
- `ConfirmedDeleteCommand` performs the actual deletion only after explicit confirmation has been received

This keeps the destructive part of the operation isolated and ensures that a student is only removed after the user has clearly confirmed the action.

<box type="info" seamless>

**Relevant diagram:** Delete confirmation workflow.

<puml src="diagrams/DeleteConfirmationActivityDiagram.puml" width="600" />

</box>

#### Design considerations

A simpler design would have been to delete the student immediately after a valid `delete` command. However, this was rejected because deletion is irreversible at the command level and a user may accidentally target the wrong student, especially when working quickly with many similar records.

TeachAssist therefore requires an explicit confirmation step for `delete`.

This differs from `clear`, which does not require confirmation. The rationale is that `clear` is a deliberate bulk-action command whose purpose is already explicit from the command word itself, whereas `delete` is more prone to accidental misuse, such as deleting the wrong student by index or entering the wrong identity fields. Requiring confirmation for `delete` therefore improves safety where the risk of unintended removal is higher.

### Feature: Update Progress

#### Overview

The `updateprogress` command allows TAs to record a student's current academic or follow-up status in TeachAssist.

This helps TAs quickly identify which students are doing well, which students may need support, and which students require closer monitoring. By storing progress directly in each student record, TeachAssist makes it easier to keep track of follow-up priorities across multiple students.

#### Progress representation

TeachAssist represents progress using a `Progress` enum in the model.

The supported progress values are:
- `ON_TRACK`
- `NEEDS_ATTENTION`
- `AT_RISK`
- `NOT_SET`

`NOT_SET` is the default value and represents the absence of an explicitly assigned progress status. It is used internally in the model, and no progress label is shown in the UI when a student's progress is `NOT_SET`.
Using an enum ensures that only valid progress values can be stored, which simplifies validation and prevents inconsistent states.

#### Implementation

The `updateprogress` feature is implemented using `ProgressCommand`, `ProgressCommandParser`, the `Progress` enum, and the model's person update mechanism.

When the user enters an `updateprogress` command, `AddressBookParser` delegates parsing to `ProgressCommandParser`, which parses the target student index and the new progress value before constructing a `ProgressCommand`.

During execution, `ProgressCommand` retrieves the target student from the current filtered student list, creates an updated `Person` object with the new progress value, and replaces the original student in the model.

If the specified progress value is `NOT_SET`, the student's progress is effectively cleared.

<box type="info" seamless>

**Note:** The sequence diagram for command execution is similar to the general command execution flow shown in the [Logic component](#logic-component) section above.

</box>

#### UI integration

Progress is displayed on each student card in the UI as a progress label. The label is colour-coded to help TAs quickly distinguish student status across `ON_TRACK`, `NEEDS_ATTENTION`, and `AT_RISK`. If the progress value is `NOT_SET`, no progress label is shown. This design keeps the UI uncluttered while still surfacing important student status information when it is available.

#### Design considerations

A key design consideration was how to represent progress in the model. One possible approach was to store progress as a plain string. However, this would require repeated string validation and would make invalid values easier to introduce. The chosen approach was to represent progress using an enum, which ensures that only valid progress states can exist and makes the implementation easier to maintain.

Another design consideration was whether to show `NOT_SET` explicitly in the UI. Showing `NOT_SET` as a visible label would make the implementation more uniform, but it would also add unnecessary visual clutter for students whose progress has not been set. The chosen design hides `NOT_SET` in the UI, so only meaningful progress statuses are shown.

### Feature: Mark Attendance Command

#### Overview

The `marka` command allows tutors to record or update a student’s attendance for a specific week. This feature enables per-week attendance tracking instead of aggregate counts, providing finer control over tutorial participation records.

This command operates on a single student identified by index from the currently displayed list and updates the attendance status for one week.

Supported statuses:
- `Y` → Present
- `A` → Absent
- `N` → Not marked

**Format:**
marka <INDEX> wk/<WEEK_NUMBER> s/<STATUS>

**Example:**
marka 1 wk/5 s/Y


#### Implementation

The mark attendance feature is implemented using:
- MarkAttendanceCommand
- MarkAttendanceCommandParser
- Week
- WeekList

#### Execution Flow

1. LogicManager calls AddressBookParser
2. MarkAttendanceCommandParser:
    - Tokenizes input using prefixes `wk/` and `s/`
    - Validates:
        - Index is present in preamble
        - Required prefixes exist
        - No duplicate prefixes
        - No unknown prefixes
        - All required values are provided
    - Parses:
        - Index
        - Week number
        - Attendance status (`Week.Status`)
3. MarkAttendanceCommand is created
4. MarkAttendanceCommand#execute(Model) is invoked

Execution steps:

1. Validate that the student index is within the filtered list
    - If invalid → throw CommandException

2. Validate that the week number is within valid range
    - If invalid → throw CommandException

3. Retrieve and copy the student’s WeekList to avoid mutating original state

4. Check whether the selected week is cancelled
    - If cancelled → throw CommandException  
      (Cancelled weeks cannot be modified under any attendance operation rule)

5. Apply attendance update based on status:
    - `Y` → markWeekAsAttended
    - `A` → markWeekAsAbsent
    - `N` → markWeekAsDefault

6. Handle invalid state transitions:
    - If the same status is already set → throw CommandException

7. Create updated Person with modified WeekList

8. Replace original Person in model using:
   model.setPerson(target, updatedPerson)

9. Return success message


#### Model-Level Logic

WeekList update behavior:

- WeekList is copied before modification to preserve immutability
- Individual Week objects enforce state rules:
    - Prevent redundant updates
    - Prevent modification of cancelled weeks
    - Maintain internal consistency of attendance state

#### Key Behaviours

- **Strict index validation**
    - Invalid student index results in CommandException

- **Week boundary validation**
    - Week index must be within valid range (1–13)

- **Cancelled week protection**
    - Attendance cannot be modified if the week is cancelled

- **Duplicate state protection**
    - Re-applying the same attendance status is rejected

- **Immutability**
    - Updates are performed via copying WeekList and replacing Person


#### Design Considerations

**Aspect: Enforcement of business rules (cancelled weeks)**

Current choice: Command-layer validation before model update
- Pros:
    - Provides immediate and clear user feedback
    - Prevents invalid state transitions early
- Cons:
    - Slight duplication with Week-level internal checks

Alternative: Allow Week class to silently ignore invalid updates
- Pros:
    - Simpler command logic
- Cons:
    - Reduces transparency and makes debugging harder


**Aspect: State update strategy**

Current choice: Copy-on-write (WeekList duplication before update)
- Pros:
    - Preserves immutability of Person objects
    - Prevents unintended side effects across references
- Cons:
    - Slight performance overhead due to object copying

Alternative: Direct mutation of WeekList
- Pros:
    - More efficient
- Cons:
    - Risk of shared-state bugs and inconsistent UI updates

**Aspect: Responsibility separation**

- Parser:
    - Handles only syntax-level validation
- Command:
    - Handles semantic validation and business rules
- Model:
    - Performs state updates only after validation

This separation ensures clear layering and maintainability of the system.


#### Sequence diagram

The following diagram shows how attendance input is parsed, validated, and applied to the target student.
<puml src="diagrams/MarkAttendanceSequenceDiagram.puml" width="600" />


### Feature: Cancel Week Command

#### Overview

The `cancelw` command marks a specific week as cancelled for all students in a given course and tutorial group.

A cancelled week:
- Cannot be marked for attendance
- Is excluded from attendance calculations

**Format:**
cancelw crs/<COURSE_ID> tg/<TUTORIAL_GROUP> wk/<WEEK_NUMBER>

**Example:**
cancelw crs/CS2103T tg/T01 wk/5


#### Implementation

The cancel week feature is implemented using:
- CancelWeekCommand
- CancelWeekCommandParser
- ModelManager#addCancelledWeek
- WeekList#markAsCancelled


#### Execution Flow

1. LogicManager calls AddressBookParser
2. CancelWeekCommandParser:
    - Tokenizes input using prefixes `crs/`, `tg/`, `wk/`
    - Validates:
        - All required prefixes are present
        - No duplicate prefixes
        - No unknown prefixes
        - No unexpected preamble
    - Parses:
        - CourseId
        - TGroup
        - Week index
3. CancelWeekCommand is created
4. CancelWeekCommand#execute(Model) is invoked

Execution steps:

1. Validate that the course–tutorial group exists
    - If not found → throw CommandException

2. Validate that the week number is within valid range
    - If invalid → throw CommandException

3. Check whether the week is already cancelled
    - If already cancelled → throw CommandException

4. Update model via:
   model.addCancelledWeek(courseId, tGroup, weekIndex)

#### Model-Level Logic

ModelManager#addCancelledWeek:

1. Construct key: <courseId>-<tGroup>
2. Retrieve or initialise cancelled week set
3. Add week index to cancelledWeeksMap
4. Propagate cancellation to all matching students:
    - Copy each student's WeekList
    - Mark the week as cancelled
    - Replace updated Person in model
5. Persist updated cancellation state to AddressBook


#### Key Behaviours

- **Strict validation**
    - Invalid course–tutorial pair results in CommandException
    - Already cancelled week results in CommandException

- **Batch update**
    - Cancellation is applied consistently to all students in the same tutorial group

- **State preservation**
    - Previous attendance status is stored inside `Week` before cancellation


#### Design Considerations

**Aspect: Consistency of cancellation state**

Current choice: Centralised `cancelledWeeksMap` with propagation to WeekList
- Pros:
    - Ensures consistent view across all students
    - Efficient lookup for cancellation status
- Cons:
    - Requires careful synchronisation between map and WeekList

Alternative: Store cancellation only in WeekList
- Pros:
    - Simpler data ownership model
- Cons:
    - Harder to query and maintain group-level cancellation state


**Aspect: Validation strategy**

Current choice: Command-layer validation with explicit exceptions
- Pros:
    - Provides immediate and clear user feedback
    - Prevents invalid state changes early
- Cons:
    - Slight duplication with model-level safety checks

---


### Feature: Uncancel Week Command

#### Overview

The `uncancelw` command reverses a previously cancelled week for a given course and tutorial group.

After uncancelling:
- The week becomes available for attendance marking
- The previously stored attendance status is restored

<box type="info" seamless></box>

**Note:** Transience of Previous Week Status

The `prevStatus` field in `Week` is **transient and not persisted to storage**. 
It is only used during runtime to support the uncancel operation, allowing a cancelled week to restore its previous attendance state.

This design is intentional because:

- `prevStatus` is only meaningful during a single session’s cancellation lifecycle
- Persisting it would unnecessarily increase storage complexity and risk stale state restoration

As a result, uncancelling after restart restores to default.

</box></box>


**Format:**
uncancelw crs/<COURSE_ID> tg/<TUTORIAL_GROUP> wk/<WEEK_NUMBER>

**Example:**
uncancelw crs/CS2103T tg/T01 wk/5


#### Implementation

The uncancel week feature is implemented using:
- UnCancelWeekCommand
- UnCancelWeekCommandParser
- ModelManager#removeCancelledWeek
- WeekList#markAsUncancelled


#### Execution Flow

1. LogicManager calls AddressBookParser
2. UnCancelWeekCommandParser:
    - Tokenizes input using prefixes `crs/`, `tg/`, `wk/`
    - Validates:
        - All required prefixes are present
        - No duplicate prefixes
        - No unexpected preamble
        - No unknown prefixes
    - Parses:
        - CourseId
        - TGroup
        - Week index
3. UnCancelWeekCommand is created
4. UnCancelWeekCommand#execute(Model) is invoked

Execution steps:

1. Validate that the course–tutorial group exists
    - If not found → throw CommandException

2. Validate that the week number is within valid range
    - If invalid → throw CommandException

3. Check whether the week is currently cancelled
    - If not cancelled → throw CommandException

4. Update model via:
   model.removeCancelledWeek(courseId, tGroup, weekIndex)


#### Model-Level Logic

ModelManager#removeCancelledWeek:

1. Locate cancellation entry in cancelledWeeksMap
2. Remove week index from map
3. Propagate uncancellation to all matching students:
    - Copy each student's WeekList
    - Restore previous status using Week#markAsUncancelled
    - Replace updated Person in model
4. Persist updated cancellation state

#### Key Behaviours

- **Strict validation**
    - Only cancelled weeks can be uncancelled
    - Invalid operations result in CommandException

- **State restoration**
    - Previous attendance status is restored using Week.prevStatus

- **Batch update**
    - Ensures all students in the tutorial group remain consistent

#### Design Considerations

**Aspect: Restoring previous attendance state**

Current choice: Store previous status inside Week
- Pros:
    - Accurate restoration of original attendance
    - Preserves user input history
- Cons:
    - Additional state management complexity

Alternative: Reset to default status
- Pros:
    - Simpler implementation
- Cons:
    - Loss of original attendance information

**Aspect: Validation responsibility**

Current choice: Command-layer validation with model queries
- Pros:
    - Clear separation of concerns
    - User-friendly error messages
- Cons:
    - Some duplication with model safeguards

### Feature: Remark Command
#### Overview

The `remark` command allows a teaching assistant to attach a remark to a specific student. This is useful for recording contextual observations that are not captured by the standard student fields (e.g, class participation, submission behaviour, consultation follow-ups, or other teaching-related comments)

The command targets a student by their displayed student index in the current person list and adds a remark to that student. The remark is intended to help TAs keep track of student-specific context across multiple interactions. The command word is `remark`, and its expected format is:

`remark INDEX txt/REMARK`

For example, `remark 1 txt/Participates actively in class` adds a new remark `Participates actively in class` to the first student in the displayed list.

#### Remark representation

In the model, a remark is represented as a dedicated `Remark` object rather than plain text alone. Each remark stores:
- the remark text
- the date on which the remark was created

This design allows each remark to carry basic metadata in addition to its content. In the current implementation, the creation date is automatically assigned using `LocalDate.now()` when the command is parsed. This means the user only provides the text of the remark, while the system records the date implicitly.

- Remarks are stored in each `Person` as a `List<Remark>`.
- In storage, `JsonAdaptedPerson` stores `remarks` as a `List<JsonAdaptedRemark>`.
- Each `JsonAdaptedRemark` contains a `text` field and a `date` field.
- During deserialization, each adapted remark is converted back into a model-level `Remark` object and reattached to the corresponding person.

#### Implementation

When `RemarkCommand#execute` is called, the command first retrieves the currently filtered person list from the model. It checks whether the provided index is within bounds; if not, it throws a `CommandException` using `Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX`. Otherwise, it retrieves the target `Person` from the displayed list, constructs a copy of that person, adds the new remark to the copied person, and updates the model using `model.setPerson(personToEdit, editedPerson)`. A success message is then returned to the user.

An important implementation detail is that the command does not mutate the original `Person` object in place. Instead, it works on a copied `Person` instance and replaces the old person in the model using `setPerson(...)`. This makes the update explicit and aligns the remark feature with the application's general update pattern.

The following sequence diagram provides a simplified view of how the updated address book state is persisted through the storage layer after execution of the `remark` command.

![Remark command sequence diagram](images/RemarkSequenceDiagram.puml)


### Feature: View Command

#### Overview

The `view` command opens a dedicated panel on the right-hand side of the UI to display the full details of a single student. This provides a comprehensive, at-a-glance summary including the student's attendance record and all associated remarks, which are not fully visible in the main list. The same panel also opens when the user clicks on a student row in the list.

#### Implementation

The `view` feature is implemented by `ViewCommand`, `ViewCommandParser`, the `ViewWindow` UI component, and the auto-sync logic in `MainWindow`.

1. **`ViewCommand`**: Retrieves the `Person` object from the model's filtered list at the specified index. It returns a `CommandResult` that carries a reference to the `Person` to be viewed. `CommandResult#shouldShowView()` returns `true` when this reference is non-null.
2. **`MainWindow#handleCommandResult`**: Checks `shouldShowView()` on the returned `CommandResult`. If true, it calls `handleView(person)`, which passes the `Person` to `ViewWindow#setPerson` and lazily adds the `ViewWindow` root node to `viewWindowPlaceholder` if not already present. It also syncs the list selection highlight to the viewed student.
3. **`ViewWindow#setPerson`**: Populates the UI labels with the student's metadata (name, student ID, course, tutorial group) and dynamically generates a row of `Label` nodes in a `GridPane` for each remark.
4. **Click-to-view**: `MainWindow#fillInnerParts` registers a mouse click handler on the `PersonListPanel` that calls `handleView(selectedPerson)`, providing the same view behaviour without typing a command.

#### View Window Auto-Sync

After every command execution, `MainWindow#updateViewWindowAfterCommand()` ensures the view panel stays in sync with the underlying data. This runs regardless of which command was executed.

The logic is:

1. If the `ViewWindow` is not currently visible (i.e. `viewWindowPlaceholder` is empty), do nothing.
2. Otherwise, search the current filtered person list for the student being viewed, using `ViewWindow#isViewing(Person)`. This method compares using `Person#isSamePerson` (name + course + tutorial group) rather than `equals`, so the view persists even when identity fields like student ID or email are edited.
3. If the student is found in the list, refresh `ViewWindow` with the updated `Person` object and re-select them in the list.
4. If the student is not found (e.g. deleted, or filtered out), clear the `ViewWindow`, remove it from the display, and clear the list selection highlight.


### \[Proposed\] Batch Attendance Marking

#### Motivation

Currently, the `marka` command marks attendance for a single student at a time. In practice, TAs typically mark attendance for an entire tutorial group in one sitting. For a class of 20+ students this requires 20+ individual `marka` commands — slow and error-prone. A batch marking command would allow TAs to mark all students in a course–tutorial group for a given week in a single command.

#### Proposed command format

`markall crs/COURSE_ID tg/TUTORIAL_GROUP week/WEEK_NUMBER sta/STATUS`

Example: `markall crs/CS2103T tg/T01 week/3 sta/Y` marks week 3 as attended for every student in CS2103T T01.

#### Proposed implementation

The feature would introduce two new classes: `MarkAllCommand` and `MarkAllCommandParser`.

**Parsing phase:**
`MarkAllCommandParser` validates that all four prefixes (`crs/`, `tg/`, `week/`, `sta/`) are present and parses their values into a `CourseId`, `TGroup`, week `Index`, and `Week.Status`.

**Execution phase:**
`MarkAllCommand#execute(Model)` proceeds as follows:

1. Retrieve the full person list from the model.
2. Filter to students matching the given `CourseId` and `TGroup`.
3. If no students match, throw a `CommandException`.
4. For each matching student:
   a. Copy the student's `WeekList`.
   b. Check the target week's status. If the week is **cancelled**, skip this student and add them to a skipped list.
   c. Otherwise, mark the week with the given status and replace the student in the model via `Model#setPerson`.
5. Return a `CommandResult` summarising how many students were marked and how many were skipped due to cancellation.

This is a **partial-success** design: students with cancelled weeks are skipped rather than causing the entire command to fail. This is preferable in a batch context because a single cancelled week (e.g. from a makeup tutorial) should not block the TA from marking the rest of the class.

The sequence diagram below illustrates the execution flow, including the loop over matching students and the alt branch for cancelled weeks.

<puml src="diagrams/MarkAllSequenceDiagram.puml" width="750" />

#### Design considerations

**Aspect: Handling cancelled weeks in a batch operation**

* **Chosen approach — Partial success (skip and report):** Each student is processed independently. Students whose target week is cancelled are silently skipped and reported in the summary. This is the most practical behaviour for TAs: they can mark the whole class and review the skipped list afterward.
* **Alternative — All-or-nothing (transactional):** If any student's week is cancelled, the entire batch fails and no attendance is updated. This is safer against inconsistency, but impractical — a single cancelled week would force the TA to manually mark every other student individually, defeating the purpose of the batch command.

**Aspect: Matching students by course and tutorial group vs. by current filtered list**

* **Chosen approach — Explicit `crs/` and `tg/` prefixes:** The command always targets a specific course–tutorial group pair regardless of the current displayed list. This makes the command self-contained and deterministic — the same command always affects the same students.
* **Alternative — Operate on the current filtered list:** `markall week/3 sta/Y` would mark all currently displayed students. This is more flexible but also more dangerous: the TA might forget they have an active filter, leading to unintended partial marking. The explicit approach is safer for a batch write operation.


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
| `***` | TA managing multiple students | add remarks to a student’s record | keep track of important observations, follow-up actions, and teaching-related context |
| `***` | TA managing multiple students | delete a remark from a student’s record | remove outdated, incorrect, or no longer relevant remarks |
| `***` | TA who conducts consultations | view a student’s remarks and details | prepare for future consultations more effectively |
| `**` | TA managing many students | keep remarks together with each student record | avoid scattering notes across separate apps or documents |
| `**` | TA managing multiple tutorial groups | keep all students across different courses and tutorial groups in one application | avoid maintaining multiple spreadsheets or lists |
| `**` | careful TA | receive clear error messages when a command format is invalid | correct mistakes quickly |
| `**` | careful TA | be prevented from adding duplicate student records | maintain clean and consistent data |
| `**` | TA | clear the current filter | return to the full student list after narrowing it down |


### Use cases
<help>

<Add student>
**Use Case: UC01 – Add Student**<br>
**Actor:** User<br>
**MSS:**
1. User enters the command to add a student.
2. User provides the student’s name, student ID, course, tutorial group, and optional Email and Telegram username.
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

<find student>

<filter student>

<edit student>
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

<mark attendance>
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

<cancelweek>

<uncancelweek>

**Use Case: UC08 – Update Student Progress Status**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to update a student’s progress status.
2. TeachAssist validates the command and identifies the target student.
3. TeachAssist updates the student’s progress status.
4. TeachAssist displays a success message confirming the update.
5. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 2a. The specified student does not exist.
    * 2a1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 2b. The specified progress status is invalid.
    * 2b1. TeachAssist informs the user of the valid progress statuses.
    * Use case ends.

<add remark>
**Use Case: UC06 – Add remark to student**<br>
**Actor:** User<br>
**MSS:**

1. User views the student list.
2. User enters the command `remark INDEX txt/REMARK`.
3. TeachAssist adds the remark with the current date to the specified student's record.
4. TeachAssist shows a success message confirming that the remark was added.

**Extensions:**

* 2a. The index is missing, invalid, or out of range.
    * 2a1. TeachAssist shows an error message.
    * Use case ends.
* 2b. The `txt` prefix is missing.
    * 2b1. TeachAssist shows an error message.
    * Use case ends.
* 2c. The remark text is empty or exceeds the allowed length.
    * 2c1. TeachAssist shows an error message.
    * Use case ends.

<delete remark>

<view student>
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
4. User enters `yes`.
5. TeachAssist deletes the student record from the system.
6. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 2a. The specified student does not exist.
    * 2a1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 4a. The user enters `no`.
    * 4a1. TeachAssist cancels the deletion.
    * Use case ends.
* 4b. The user enters another command instead of `yes` or `no`.
    * 4b1. TeachAssist cancels the pending deletion.
    * 4b2. TeachAssist processes the new command.
    * Use case resumes from the relevant step of the new command.
* 4c. The user enters an invalid confirmation response that is not a recognised command.
    * 4c1. TeachAssist cancels the pending deletion.
    * 4c2. TeachAssist displays an error message.
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

**Use Case: UC16 – Mark Student Attendance**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to mark a student’s attendance for a specific week with a status.
2. TeachAssist validates the student index, week number, and attendance status.
3. TeachAssist updates the student’s attendance record for the specified week.
4. TeachAssist confirms the update.
5. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.

* 2a. The specified student does not exist.
    * 2a1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.

* 2b. The specified week number is invalid.
    * 2b1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.

* 2c. The specified week is cancelled.
    * 2c1. TeachAssist informs the user that cancelled weeks cannot be modified.
    * Use case ends.

* 2d. The attendance status is invalid or already set.
    * 2d1. TeachAssist informs the user of valid statuses or that the status is already assigned.
    * Use case ends.

---

**Use Case: UC17 – Cancel Tutorial Week**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to cancel a specific week for a course and tutorial group.
2. TeachAssist validates the course ID, tutorial group, and week number.
3. TeachAssist marks the specified week as cancelled for all students in the tutorial group.
4. TeachAssist updates the system state.
5. TeachAssist confirms the cancellation.
6. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.

* 2a. The course or tutorial group does not exist.
    * 2a1. TeachAssist informs the user that the course-tutorial pair cannot be found.
    * Use case ends.

* 2b. The week number is invalid.
    * 2b1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.

* 2c. The week is already cancelled.
    * 2c1. TeachAssist informs the user that the week is already cancelled.
    * Use case ends.

---

**Use Case: UC18 – Uncancel Tutorial Week**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to uncancel a specific week for a course and tutorial group.
2. TeachAssist validates the course ID, tutorial group, and week number.
3. TeachAssist restores the previously cancelled week to active status for all students in the tutorial group.
4. TeachAssist updates the system state.
5. TeachAssist confirms the uncancellation.
6. Use case ends.

**Extensions**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.

* 2a. The course or tutorial group does not exist.
    * 2a1. TeachAssist informs the user that the course-tutorial pair cannot be found.
    * Use case ends.

* 2b. The week number is invalid.
    * 2b1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.

* 2c. The week is not cancelled.
    * 2c1. TeachAssist informs the user that the week is not cancelled.
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

   **Test case:** Download the jar file and copy to an empty folder. Run the app using java -jar TeachAssist.jar.

   **Expected behavior:** The GUI should launch with a default window size and populate with sample data.

2. Saving window preferences

   **Test case:** Resize the window to a different size, or move it to a different part of the screen. Close the app using the exit command and relaunch it.

   **Expected behavior:** The app window should reappear with the same size and at the same position as it was before closing.

3. Shutdown

    **Test case:** Enter exit into the command box

    **Expected behavior**: The application closes immediately and the terminal process terminates.

### Adding a student

1. Adding a student with valid fields

    1. **Test case:** `add n/John Doe id/A0123456X e/johnd@u.nus.edu crs/CS2103T tg/T01 tel/@johndoe`

    2. **Expected behaviour:** A new student is added to the list. Success message shown: `"New person added: John Doe; Student ID: A0123456X; Email: johnd@u.nus.edu; Course ID: CS2103T; TGroup: T01; Tele: @johndoe"`.

2. Adding a student with missing required fields

    1. **Test case:** `add n/John Doe id/A0123456X` (missing email, course, and tutorial group)

    2. **Expected behaviour:** Command rejected with an error message showing the correct usage format. All of `n/`, `id/`, `e/`, `crs/`, `tg/` are required.

3. Adding a student with invalid field values

    1. **Test case:** `add n/John123 id/A0123456X e/johnd@u.nus.edu crs/CS2103T tg/T01`

    2. **Expected behaviour:** Command rejected with an error message indicating the name constraint (names should only contain alphabetical characters and spaces).

4. Adding a duplicate student

    1. **Test case:** Add a student that already exists in the list (same student ID, course, and tutorial group as an existing entry).

    2. **Expected behaviour:** Command rejected with error message: `"This person already exists in the address book"`.

### Editing a student

1. Editing a student with valid fields

    1. **Test case:** `edit 1 n/Jane Doe e/janed@u.nus.edu`

    2. **Expected behaviour:** The student at index 1 has their name updated to "Jane Doe" and email updated. Success message shown: `"Edited Person: Jane Doe; Student ID: ...; Email: janed@u.nus.edu; ..."`.

2. Editing a student with invalid fields

    1. **Test case:** `edit 1 e/invalid-email-format`

    2. **Expected behaviour:** Command rejected with an error message indicating the email constraint.

3. Editing a non-existent student

    1. **Test case:** `edit 999 n/Jane Doe` (where index 999 exceeds the displayed list size)

    2. **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`.

4. Editing with missing edit fields

    1. **Test case:** `edit 1` (no fields to edit specified)

    2. **Expected behaviour:** Command rejected with error message: `"At least one field to edit must be provided."`.


### Help command (`help`)

1. Opening the Help Window

    1. **Test case:** Type `help` and press Enter.

    2. **Expected behaviour:** The Help window opens. Result box shows message: `"Opened help window."`.

    3. **Test case:** Press the F1 key (or fn + F1 on Mac).

    4. **Expected behaviour:** The Help window opens. Result box shows message: `"Opened help window."`.

    5. **Test case:** Type `help icecream` (with extra text after `help`).

    6. **Expected behaviour:** The Help window still opens (extra text is ignored). Result box shows message: `"Opened help window."`.

2. Window Focus Behavior

    1. **Prerequisite:** The Help window is already open but not minimized.

    2. **Test case:** Type `help` or press F1 again while the main window has focus.

    3. **Expected behaviour:** The existing Help window is brought to the front/focus. No duplicate window is created.


### Find command (`find`)

1. Single-keyword search

    1. **Test case:** Enter `find Alice` where "Alice" exists in sample data.

    2. **Expected behaviour:** Displayed list shows students whose names contain a word starting with "Alice" (case-insensitive). Result box shows message: `"X students listed!"` where X is the number of matching students.

2. Multiple-keyword search

    1. **Test case:** Enter `find Al Bob` where both keywords match different students.

    2. **Expected behaviour:** Displayed list contains students matching any of the keywords (OR across keywords). No duplicates. Result box shows message: `"X students listed!"` where X is the number of matching students.

3. Case and prefix matching

    1. **Test case:** Enter `find ann` to match "Annabelle" and `find ANN`.

    2. **Expected behaviour:** Both commands produce the same results. Matching is case-insensitive and supports prefix matching (e.g., "ann" matches any name word starting with "ann").

4. Empty or whitespace-only query

    1. **Test case:** Enter `find` with no keywords or only whitespace.

    2. **Expected behaviour:** Command rejected with error message: `"Find command requires at least one keyword."` followed by the `find` command usage. Displayed list remains unchanged.

5. Invalid special characters

    1. **Test case:** `find A123` or `find @@@`.

    2. **Expected behaviour:** Command rejected with error message: `"Keywords should contain alphabetic characters separated by spaces only."` followed by the `find` command usage.

### Filter command (`filter`)

1. Single criterion filtering

    1. **Test case:** `filter crs/CS2103T`

    2. **Expected behaviour:** List displays only students in course CS2103T. Result box shows message: `"There are X students matching this filter."` where X is the number of matching students.

    3. **Test case:** `filter p/on_track`

    4. **Expected behaviour:** List displays only students with progress `on_track`. Result box shows message: `"There are X students matching this filter."`.

2. Multiple criteria filtering

    1. **Test case:** `filter crs/CS2103T tg/T01 abs/2`

    2. **Expected behaviour:** List displays only students who satisfy all criteria — course is CS2103T AND tutorial group is T01 AND absence count ≥ 2 (AND logic). Result box shows message: `"There are X students matching this filter."`.

3. No matches for filter

    1. **Test case:** `filter crs/CS9999` (a non-existent course).

    2. **Expected behaviour:** List becomes empty; result box shows: `"There are 0 students matching this filter."`.

4. Absence threshold checks

    1. **Test case:** `filter abs/0`

    2. **Expected behaviour:** Matches everyone (since all students have 0 or more absences). Result box shows: `"There are X students matching this filter."`.

    3. **Test case:** `filter abs/5`

    4. **Expected behaviour:** Matches only students with at least 5 absences. Result box shows: `"There are X students matching this filter."`.

    5. **Test case:** `filter abs/99`

    6. **Expected behaviour:** Command rejected with error message: `"Absence count must be an integer between 0 and 13 (inclusive)."`.

5. Invalid inputs and error handling

    1. **Test case:** `filter` (empty command, no filter criteria).

    2. **Expected behaviour:** Command rejected with error message: `"At least one filter must be provided."` followed by the filter command usage.

    3. **Test case:** `filter crs/` (missing value for course).

    4. **Expected behaviour:** Command rejected with error message: `"Course ID cannot be empty."`.

    5. **Test case:** `filter abs/xyz`

    6. **Expected behaviour:** Command rejected with error message: `"Absence count must be an integer between 0 and 13 (inclusive)."`.

    7. **Test case:** `filter p/invalid_status`

    8. **Expected behaviour:** Command rejected with error message: `"Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear."`.

### Deleting a student

1. Deleting a student by index

    1. **Test case:** `delete 1`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message: `"Are you sure you want to delete <student name>? Type 'yes' to confirm or 'no' to cancel."`.

2. Deleting a student by student details

    1. **Test case:** `delete id/A1234567X crs/CS2103T tg/T01`

    2. **Expected behaviour:** If a student matching the given `StudentId`, `CourseId`, and `TGroup` exists in the entire TeachAssist list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message asking the user to type `yes` to confirm or `no` to cancel.

3. Confirming a deletion

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter `yes`.

    2. **Expected behaviour:** The pending deletion is executed, the student is removed from TeachAssist, and a success message is shown: `"Deleted Person: <student details>"`.

4. Cancelling a deletion

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter `no`.

    2. **Expected behaviour:** The pending deletion is cancelled, no student is removed, and a cancellation message is shown: `"Delete operation cancelled."`.

5. Entering another command while deletion is pending

    1. **Test case:** Enter a valid delete command such as `delete 1`, then enter another command such as `list`.

    2. **Expected behaviour:** The pending deletion is cleared and the new command (`list`) is processed normally. No student is deleted unless the user re-enters the delete command and confirms it.

6. Deleting with invalid command format

    1. **Test case:** `delete abc`

    2. **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown indicating invalid command format.

7. Deleting with invalid index format

    1. **Test case:** `delete -1`

    2. **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown: `"Invalid index number"`.

8. Deleting a non-existent student by details

    1. **Prerequisite:** Ensure that no student in the address book matches these 3 fields: `id/A0000000Z crs/CS9999 tg/T99`

    2. **Test case:** `delete id/A0000000Z crs/CS9999 tg/T99`

    3. **Expected behaviour:** The command is rejected with error message: `"No student matching the given student ID, course ID, and tutorial group was found."`. No confirmation is requested.

### Updating progress

1. Updating progress with a valid status

    1. **Test case:** `updateprogress 1 p/on_track`

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student's progress is updated to `ON_TRACK` and a success message is shown: `"Updated progress for student: <student details>. New progress: ON_TRACK"`. Note: progress values are case-insensitive (`ON_TRACK` and `on_track` both work).

2. Updating progress with an invalid status

    1. **Test case:** `updateprogress 1 p/GOOD`

    2. **Expected behaviour:** The command is rejected, no student record is updated, and an error message is shown: `"Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear."`.

3. Updating progress with an invalid index

    1. **Test case:** `updateprogress 999 p/at_risk`

    2. **Expected behaviour:** If index `999` is outside the bounds of the current filtered list, the command is rejected, no student record is updated, and an error message is shown: `"The student index provided is invalid"`.

4. Removing progress using `clear` or `not_set`

    1. **Test case:** `updateprogress 1 p/clear` (or equivalently `updateprogress 1 p/not_set`)

    2. **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student's progress is reset to `NOT_SET`. The progress tag is removed from the student card in the UI, and a success message is shown: `"Cleared progress for student: <student details>"`.

5. Updating progress with invalid command format

    1. **Test case:** `updateprogress p/on_track` (missing index)

    2. **Expected behaviour:** The command is rejected because the required student index is missing, no student record is updated, and an error message is shown with the correct usage format.

### Marking attendance

1. Marking attendance with valid input

    1. **Test case:** `markattendance 1 week/3 sta/Y`

    2. **Expected behaviour:** Student at index 1 has week 3 marked as attended. Success message: `"Week 3 marked as Y (Present) for: <student name> (<student ID>)"`.

2. Marking attendance with invalid week

    1. **Test case:** `markattendance 1 week/20 sta/Y`

    2. **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

3. Marking attendance with cancelled week

    1. **Test case:**
       `cancelweek crs/CS2103T tg/T01 week/3`
       `markattendance 1 week/3 sta/Y`

    2. **Expected behaviour:** Command rejected with error message: `"Week 3 is cancelled and cannot be marked."`.

4. Marking attendance duplicate status

    1. **Test case:**
       `markattendance 1 week/2 sta/Y`
       `markattendance 1 week/2 sta/Y`

    2. **Expected behaviour:** Second command rejected with error message: `"Week 2 already has status 'Y' for <student name> (<student ID>)."`.

5. Marking attendance for non-existent student

    1. **Test case:** `markattendance 999 week/2 sta/Y`

    2. **Expected behaviour:** Command rejected with error message: `"The person index provided is invalid."`.

### Cancelling a week

1. Cancelling a week with valid input

    1. **Test case:** `cancelweek crs/CS2103T tg/T01 week/5`

    2. **Expected behaviour:** Week 5 cancelled for all students in CS2103T T01. Success message: `"Week 5 cancelled for course CS2103T tutorial T01."`.

2. Cancelling already cancelled week

    1. **Test case:** Run `cancelweek crs/CS2103T tg/T01 week/5` twice.

    2. **Expected behaviour:** Second command rejected with error message: `"Week 5 is already cancelled for course CS2103T tutorial T01."`.

3. Cancelling invalid week

    1. **Test case:** `cancelweek crs/CS2103T tg/T01 week/20`

    2. **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

4. Cancelling non-existent course/tutorial

    1. **Test case:** `cancelweek crs/CS9999 tg/T99 week/2`

    2. **Expected behaviour:** Command rejected with error message: `"Course CS9999 with tutorial T99 does not exist and cannot be cancelled."`.

### Uncancelling a week

1. Uncancelling valid week

    1. **Test case:**
       `cancelweek crs/CS2103T tg/T01 week/4`
       `uncancelweek crs/CS2103T tg/T01 week/4`

    2. **Expected behaviour:** Week 4 restored for all students. Success message: `"Week 4 uncancelled for course CS2103T tutorial T01."`.

2. Uncancelling non-cancelled week

    1. **Test case:** `uncancelweek crs/CS2103T tg/T01 week/3` (where week 3 has not been cancelled)

    2. **Expected behaviour:** Command rejected with error message: `"Week 3 is not cancelled for course CS2103T tutorial T01."`.

3. Uncancelling invalid week

    1. **Test case:** `uncancelweek crs/CS2103T tg/T01 week/20`

    2. **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

4. Uncancelling non-existent course/tutorial

    1. **Test case:** `uncancelweek crs/CS9999 tg/T99 week/1`

    2. **Expected behaviour:** Command rejected with error message: `"Course CS9999 with tutorial T99 does not exist and cannot be uncancelled."`.

### Adding a remark

1. Adding a remark with valid input

    1. **Test case:** `remark 1 txt/Participates actively in class`

    2. **Expected behaviour:** A remark with the text "Participates actively in class" and the current date is added to the student at index 1. Success message shown: `"Added remark to Person: <student details> Remark: Participates actively in class"`.

2. Adding a remark with missing prefix

    1. **Test case:** `remark 1 txt/` (empty remark text after prefix)

    2. **Expected behaviour:** Command rejected with error message: `"Remark text cannot be empty."`.

    3. **Test case:** `remark 1` (missing `txt/` prefix entirely)

    4. **Expected behaviour:** Command rejected with an error message showing the correct usage format.

3. Adding a remark with missing remark text

    1. **Test case:** `remark 999 txt/Some remark` (where index 999 exceeds the displayed list size)

    2. **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`.

### Viewing student details / remarks

1. Viewing a student with valid input

    1. **Test case:** Ensure a student is visible in the current displayed list, then enter `view 1` and press Enter.

    2. **Expected behaviour:** The detail pane displays the selected student's full information (name, student ID, course, tutorial group, email, tele), attendance summary and remark entries. Success message shown: `"Viewing student: <student details>"`. All fields render correctly; long text wraps or scrolls.

2. Viewing a non-existent / out-of-range index

    1. **Test case:** Enter `view 9999` (index greater than displayed list size) or `view 0`.

    2. **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`; detail pane remains unchanged.


### Listing students

1. Listing all students

    1. **Test case:** First apply a filter (e.g., `filter crs/CS2103T`), then enter `list`.

    2. **Expected behaviour:** The full student list is displayed (all filters cleared), sorted alphabetically by name. Success message shown: `"Listed all persons"`.

### Clearing the student list / sample data

1. Clearing all students

    1. **Test case:** Ensure the list has at least one student, then enter `clear`.

    2. **Expected behaviour:** All student records are removed from TeachAssist. The list becomes empty. Success message shown: `"Address book has been cleared!"`.

2. Clearing when the list is already empty

    1. **Test case:** Enter `clear` when the student list is already empty (e.g., after a previous `clear`).

    2. **Expected behaviour:** The command succeeds with the same message: `"Address book has been cleared!"`. No error is shown.

### Clearing filters

Note: There is no dedicated `clearfilter` command. To reset any active filter and return to the full student list, use the `list` command.

1. Clearing an active filter

    1. **Test case:** First apply a filter (e.g., `filter crs/CS2103T`), verify the list is filtered, then enter `list`.

    2. **Expected behaviour:** The full student list is restored, sorted alphabetically by name. All filters are cleared. Success message shown: `"Listed all persons"`.

2. Clearing when no filter is active

    1. **Test case:** Without any active filter, enter `list`.

    2. **Expected behaviour:** The full student list is displayed (unchanged). Success message shown: `"Listed all persons"`. No error is shown.

### Saving data

1. Data persistence after normal usage

    1. **Test case:** Add a student (e.g., `add n/Test Student id/A9999999Z e/test@u.nus.edu crs/CS2103T tg/T01`), then close the app using `exit` and relaunch it.

    2. **Expected behaviour:** The newly added student appears in the list after relaunching. All data modifications (adds, edits, deletes) are persisted to `data/addressbook.json`.

2. Dealing with missing data files

    1. **Test case:** Close the app, delete the `data/addressbook.json` file, then relaunch the app.

    2. **Expected behaviour:** The app launches with the default sample data, as if running for the first time. A new `data/addressbook.json` file is created upon the next data-modifying command.

3. Dealing with corrupted data files

    1. **Test case:** Close the app, open `data/addressbook.json` in a text editor and corrupt it (e.g., delete a closing brace or add invalid characters), then relaunch the app.

    2. **Expected behaviour:** The app launches with an empty student list. The corrupted data file is not loaded. A warning may be logged.

### Suggested exploratory testing

1. Combining multiple commands in sequence

    1. **Workflow:** Add a student → mark their attendance for weeks 1–3 → update their progress to `at_risk` → add a remark → use `view` to verify all data → edit their email → use `view` again to confirm the edit is reflected → delete the student with confirmation. Verify each step produces the correct success message and the data is consistent throughout.

2. Testing invalid inputs and edge cases

    1. **Workflow:** Try each command with: empty arguments, extra spaces, very long input strings, special characters in fields, index `0`, negative indices, and indices exceeding the list size. Verify that all invalid inputs produce clear, specific error messages and do not corrupt the application state.

3. Testing persistence across restarts

    1. **Workflow:** Make several data changes (add students, mark attendance, update progress, add remarks), then close the app with `exit`. Relaunch and verify that all changes persist. Then close without `exit` (e.g., close the window) and verify data is still saved.


## **Appendix: Planned Enhancements**

1.Relax student name and find command keywords validation to support special characters. Currently, the name field accepts only alphabetical characters and spaces; we plan to extend this to support names containing hyphens, apostrophes, and other common punctuation, such as “O’Connor” and “Smith-Jones.”

2.Extend find to support prefix-based search across additional fields such as student ID, email, and course, instead of names only.

3.Add support for multi-value filtering. Currently, each filter prefix accepts only a single value; we plan to extend this to allow multiple values under the same prefix in a single filter command.

4.Add support for more flexible absence filtering. Currently, absence filtering only supports values greater than or equal to a given threshold; we plan to extend this to support exact values, upper bounds, and ranges.

5. Add confirmation support for `clear`. Currently, `clear` removes all student records immediately after execution. We plan to introduce an optional confirmation workflow similar to `delete`, so that users must explicitly confirm before all records are removed. One possible implementation is to let `LogicManager` temporarily store a pending clear action after a valid `clear` command is entered, and only execute the actual clearing when the user responds with `yes`. Entering `no` or another command would cancel the pending clear action. This would reduce the risk of accidental mass deletion while keeping the command behaviour consistent with other destructive operations.
