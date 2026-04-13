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

### **AI-Assisted Tools**

- **GitHub Copilot** — used as a code-completion assistant throughout the project. Copilot suggestions were used to accelerate writing of routine boilerplate code, test cases, and JavaDoc comments. It was also used to help identify and extract duplicated logic into shared utility methods and common helper classes across command and parser implementations, reducing code duplication. All AI-generated suggestions were reviewed, tested, and adapted by the team before being committed.

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

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `add n/John id/A0123456X ...`.

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

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `ViewWindow`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `ViewWindow` is an embedded detail panel that displays a single student's full information (including remarks). It is shown inside the `MainWindow` when the user executes a `view` command or clicks a student row, and is automatically refreshed or cleared after subsequent commands to keep the displayed data in sync.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.
* automatically refreshes or clears the `ViewWindow` after every command execution — if the viewed student is still in the filtered list, the panel is refreshed with the latest data; if the student has been deleted or filtered out, the panel is cleared.


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

- It supports deleting a student either by displayed index or by exact student identity fields: `STUDENT_ID`, `COURSE_ID`, and `TUTORIAL_GROUP`.
- It introduces a confirmation step before the deletion is carried out.

This is useful because student records are important and should not be removed accidentally, especially when a TA may be managing many students across multiple courses and tutorial groups.

#### Supported delete modes

TeachAssist supports the following delete modes:

- **Delete by displayed index**
  The user deletes a student using the index shown in the current displayed student list.
  Example: `delete 1`

- **Delete by exact student details**
  The user deletes a student by specifying the student’s `STUDENT_ID`, `COURSE_ID`, and `TUTORIAL_GROUP`.
  Example: `delete id/A1234567X crs/CS2103T tg/T01`

For detail-based deletion, the match is performed against the **entire TeachAssist list**, rather than only the currently filtered list. This allows the user to delete a specific student directly even if that student is not currently visible in the displayed list.

#### Implementation

The `delete` feature is implemented primarily using `DeleteCommand`, `ConfirmedDeleteCommand`, and `LogicManager`.

<box type="info" seamless>

**Relevant diagram:** Delete-related class structure.

<puml src="diagrams/DeleteClassDiagram.puml" width="600" />

</box>

When the user enters a `delete` command, the command is first parsed into a `DeleteCommand`. However, the student is **not deleted immediately**.

Instead, `LogicManager` handles deletion as a two-stage workflow:

1. the target student is first resolved by `DeleteCommand`
2. the actual deletion only happens after the user explicitly confirms with `yes`

After parsing a valid `DeleteCommand`, `LogicManager` calls `DeleteCommand#getConfirmedCommand(model)` to create a `ConfirmedDeleteCommand`, and `DeleteCommand#getConfirmationMessage(model)` to generate the confirmation message shown to the user. The resulting `ConfirmedDeleteCommand` is then stored temporarily inside `LogicManager` as a pending confirmation command.

If the user enters `yes`, `LogicManager` executes the stored `ConfirmedDeleteCommand`. `ConfirmedDeleteCommand#execute(Model)` then performs the actual deletion through `Model#deletePerson(Person)`.

If the user enters `no`, `LogicManager` clears the stored pending command and no deletion occurs.

If the user enters some other command instead, `LogicManager` also clears the pending command. In other words, the confirmation is only valid for the immediate follow-up response, and any other input cancels the pending deletion flow.

This design separates intent resolution from destructive execution:

- `DeleteCommand` identifies which student the user intends to delete
- `ConfirmedDeleteCommand` performs the actual deletion only after explicit confirmation has been received


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


#### Design Considerations

**Aspect: Enforcement of business rules (cancelled weeks)**

* **Current choice - Command-layer validation before model update:** This gives immediate user feedback and prevents invalid state transitions from reaching the model. However, it causes slight duplication with Week-level internal checks.
* **Alternative — Allow Week class to silently ignore invalid updates:** This gives simpler command logic, but that reduces transparency and makes debugging harder.

**Aspect: State update strategy**

* **Current choice - Copy-on-write (WeekList duplication before update):** This preserves immutability of Person objects and prevents unintended side effects across references. However, it causes slight performance overhead due to object copying.
* **Alternative - Direct mutation of WeekList:** This would be more efficient but risks shared-state bugs and inconsistent UI updates.

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


#### Design Considerations

**Aspect: Consistency of cancellation state**

* **Current choice - Centralised `cancelledWeeksMap` with propagation to WeekList:** This ensures consistent view across all students and efficient lookup for cancellation status. However, it requires careful synchronisation between map and WeekList.
* **Alternative - Store cancellation only in WeekList:** This gives simpler data ownership model but is harder to query and maintain group-level cancellation state.


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


#### Design Considerations

**Aspect: Restoring previous attendance state**

* **Current choice - Store previous status inside Week:** This ensures accurate restoration of original attendance and preserves user input history. However there is additional state management complexity.
* **Alternative - Reset to default status:** This requires simpler implementation but causes loss of original attendance information.


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
| `**` | new user | open a help window listing all available commands | learn how to use the system |
| `**` | new user | view preloaded sample student data | understand how student records are structured |
| `**` | new user | clear all sample data | start using TeachAssist with my own student records |
| `***` | TA | add a student with fields such as `NAME`, `STUDENT_ID`, `COURSE_ID`, `TUTORIAL_GROUP`, `EMAIL`, and `TELEGRAM_USERNAME` | maintain complete and structured student records |
| `***` | TA | edit a student’s details | keep student records accurate and up to date |
| `***` | TA | list all students | get an overview of all the students I am managing |
| `**` | TA | find students by name keywords | quickly locate a student when I do not remember their full details |
| `**` | TA managing multiple classes | filter or narrow down the displayed student list | focus on the relevant group of students more quickly |
| `***` | TA | delete a student by index | quickly remove an incorrect or outdated student record |
| `***` | TA | delete a student by `STUDENT_ID`, `COURSE_ID`, and `TUTORIAL_GROUP` | remove a specific student without relying on the displayed index |
| `***` | careful TA | be asked to confirm before deleting a student | avoid accidentally deleting the wrong student record |
| `**` | TA managing multiple classes | distinguish students by course ID and tutorial group as well as name | avoid confusion between students from different classes or with similar names |
| `**` | careful TA | be prevented from adding duplicate student records | maintain clean and consistent data |
| `**` | careful TA | receive clear error messages when a command format is invalid | correct mistakes quickly |
| `***` | TA tracking student performance | update a student’s progress status | quickly identify which students are on track or need support |
| `**` | TA preparing for class | view a student’s progress status in the UI | understand the student’s standing at a glance |
| `***` | TA taking tutorial attendance | mark attendance for a student | keep a record of who attended class |
| `***` | TA managing a tutorial group | cancel a tutorial week for a class | reflect weeks where no tutorial was conducted |
| `**` | TA managing a tutorial group | restore a previously cancelled tutorial week | correct mistaken cancellations or resume normal attendance tracking |
| `***` | TA managing multiple students | add remarks to a student’s record | keep track of important observations, follow-up actions, and teaching-related context |
| `***` | TA managing multiple students | delete a remark from a student’s record | remove outdated, incorrect, or no longer relevant remarks |
| `***` | TA managing multiple students | view a student’s full details and remarks | quickly review the student’s record before teaching or follow-up |
| `**` | TA managing many students | keep remarks together with each student record | avoid scattering notes across separate apps or documents |
| `**` | TA managing multiple tutorial groups | keep all students across different courses and tutorial groups in one application | avoid maintaining multiple spreadsheets or lists |
| `**` | TA | return to the full student list after using find or filter | continue working with all students again |


### Use cases

**Use Case: UC01 – View Help**<br>
**Actor:** User<br>
**MSS:**
1. User enters the `help` command.
2. TeachAssist opens the Help Window, displaying the summary of available commands and a link to the User Guide.
3. Use case ends.

**Extensions:**

* 1a. The Help Window is already open.
    * 1a1. TeachAssist brings the existing Help Window into focus.
    * Use case ends.


**Use Case: UC02 – Add Student**<br>
**Actor:** User<br>
**MSS:**
1. User enters the command to add a student with the required details.
2. TeachAssist creates the student record.
3. TeachAssist adds the student to the student list.
4. TeachAssist confirms that the student has been added.
5. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The student would duplicate an existing record because the same `STUDENT_ID`, `EMAIL`, or `TELEGRAM_USERNAME` is already used for the same `COURSE_ID` and `TUTORIAL_GROUP`.
    * 1b1. TeachAssist shows an error message, informing the user that the student already exists.
    * Use case ends.


**Use Case: UC03 – Find Students by Name**<br>
**Actor:** User<br>
**MSS:**

1. User enters a `find` command with one or more name keywords.
2. TeachAssist searches the student list for matching students.
3. TeachAssist updates the displayed list to show only matching students.
4. TeachAssist displays a result message showing the number of students found.
5. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 2a. No students match any of the keywords.
    * 2a1. TeachAssist displays an empty list and a message indicating 0 students found.
    * Use case ends.


**Use Case: UC04 – Filter Student List**<br>
**Actor:** User<br>
**MSS:**

1. User enters a `filter` command with one or more criteria.
2. TeachAssist applies specified filter criteria to the student list.
3. TeachAssist updates the displayed list to show only matching students.
4. TeachAssist displays a result message showing the number of students matching the filter.
5. Use case ends.

**Extensions:**

* 1a. The user enters `filter` with no criteria at all.
    * 1a1. TeachAssist displays an error message stating that at least one filter must be provided.
    * Use case ends.
* 1b. A provided prefix is missing a value.
    * 1b1. TeachAssist displays an error message indicating the missing value.
    * Use case ends.
* 1c. One or more filter values are invalid.
    * 1c1. TeachAssist displays an error message indicating the valid values or range.
    * Use case ends.
* 2a. No student matches the filter criteria.
    * 2a1. TeachAssist displays an empty list and a message indicating 0 students matching the filter.
    * Use case ends.


**Use Case: UC05 – Edit Student**<br>
**Actor:** User<br>
**MSS:**
1. User enters the `edit` command specifying a target student and the fields to update.
2. TeachAssist updates the student record with the provided changes.
3. TeachAssist confirms that the student has been updated.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the specified student could not be found.
    * Use case ends.


**Use Case: UC06 – Mark Student Attendance**<br>
**Actor:** User<br>
**MSS:**

1. User enters the command to mark a student's attendance for a specific student week with a status.
2. TeachAssist updates the student's attendance record for the specified student and week.
3. TeachAssist confirms the updated attendance status.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 1c. The specified week number is invalid.
    * 1c1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.
* 1d. The specified week is cancelled.
    * 1d1. TeachAssist informs the user that cancelled weeks cannot be modified.
    * Use case ends.
* 1e. The attendance status is invalid or already set.
    * 1e1. TeachAssist informs the user of valid statuses or that the status is already assigned.
    * Use case ends.


**Use Case: UC07 – Cancel Tutorial Week**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to cancel a specific week for a course and tutorial group.
2. TeachAssist marks the specified week as cancelled for all students in the tutorial group.
3. TeachAssist confirms the cancellation.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The course or tutorial group does not exist.
    * 1b1. TeachAssist informs the user that the course-tutorial pair cannot be found.
    * Use case ends.
* 1c. The week number is invalid.
    * 1c1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.
* 1d. The week is already cancelled.
    * 1d1. TeachAssist informs the user that the week is already cancelled.
    * Use case ends.


**Use Case: UC08 – Uncancel Tutorial Week**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to uncancel a specific week for a course and tutorial group.
2. TeachAssist restores the previously cancelled week to active status for all students in the tutorial group.
3. TeachAssist confirms the uncancellation.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The course or tutorial group does not exist.
    * 1b1. TeachAssist informs the user that the course-tutorial pair cannot be found.
    * Use case ends.
* 1c. The week number is invalid.
    * 1c1. TeachAssist informs the user that the week number is out of range.
    * Use case ends.
* 1d. The week was not cancelled.
    * 1d1. TeachAssist informs the user that the week was not cancelled.
    * Use case ends.


**Use Case: UC09 – Update Student Progress Status**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to update a student’s progress status.
2. TeachAssist updates the student’s progress status.
3. TeachAssist displays a success message confirming the update.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 1c. The specified progress status is invalid.
    * 1c1. TeachAssist informs the user of the valid progress statuses.
    * Use case ends.


**Use Case: UC10 – Add Remark to Student**<br>
**Actor:** User<br>
**MSS:**

1. User enters the command to add a remark to a specific student.
2. TeachAssist adds the remark with the current date to the specified student's record.
3. TeachAssist shows a success message confirming that the remark was added.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The remark text is empty or exceeds the allowed length.
    * 1b1. TeachAssist displays an error message.
    * Use case ends.


**Use Case: UC11 – Delete Remark from Student**<br>
**Actor:** User<br>
**MSS:**

1. User enters the command to delete a remark from a specific student.
2. TeachAssist removes the specified remark from the specified student's record.
3. TeachAssist shows a success message confirming that the remark was deleted.
4. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist displays an error message indicating that the student index provided is invalid.
    * Use case ends.
* 1c. The specified remark does not exist.
    * 1c1. TeachAssist informs the user that the remark index is invalid.
    * Use case ends.


**Use Case: UC12 – View Student Details**<br>
**Actor:** User<br>
**MSS:**

1. User enters the `view` command with a student index.
2. TeachAssist displays the student's full details and remarks in the View Window.
3. TeachAssist highlights the corresponding student row in the list.
4. TeachAssist displays a success message confirming which student is being viewed.
5. Use case ends.

**Extensions:**

* 1a. The index is invalid or out of range.
    * 1a1. TeachAssist displays an error message.
    * Use case ends.


**Use Case: UC13 – Delete Student**<br>
**Actor:** User<br>
**MSS:**

1. User enters a command to delete a student.
2. TeachAssist displays the student details and asks the user to confirm the deletion.
3. User enters `yes`.
4. TeachAssist deletes the student record from the system.
5. Use case ends.

**Extensions:**

* 1a. The command format is invalid.
    * 1a1. TeachAssist displays an error message and the correct command format.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student record cannot be found.
    * Use case ends.
* 3a. The user enters `no`.
    * 3a1. TeachAssist cancels the deletion.
    * Use case ends.
* 3b. The user enters another command instead of `yes` or `no`.
    * 3b1. TeachAssist cancels the pending deletion.
    * 3b2. TeachAssist processes the new command.
    * Use case resumes from the relevant step of the new command.
* 3c. The user enters an invalid confirmation response that is not a recognised command.
    * 3c1. TeachAssist cancels the pending deletion.
    * 3c2. TeachAssist displays an error message.
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

### Adding a student (`add`)

1. Adding a student with valid fields

    **Test case:** `add n/John Doe id/A0123456X e/johnd@u.nus.edu crs/CS2103T tg/T01 tel/@johndoe`

    **Expected behaviour:** A new student is added to the list. Success message shown: `"New person added: John Doe; Student ID: A0123456X; Email: johnd@u.nus.edu; Course ID: CS2103T; TGroup: T01; Tele: @johndoe"`.

2. Adding a student with missing required fields

    **Test case:** `add n/John Doe id/A0123456X` (missing `COURSE_ID` and `TUTORIAL_GROUP`)

    **Expected behaviour:** Command rejected with an error message showing the correct usage format. All of `n/`, `id/`, `crs/`, `tg/` are required.

3. Adding a student with invalid field values

    **Test case:** `add n/John123 id/A0123456X e/johnd@u.nus.edu crs/CS2103T tg/T01`

    **Expected behaviour:** Command rejected with an error message indicating the name constraint (names should only contain alphabetical characters and spaces).

4. Adding a duplicate student

    **Test case:** Add a student whose `STUDENT_ID`, `EMAIL`, or `TELEGRAM_USERNAME` matches an existing student in the same `COURSE_ID` and `TUTORIAL_GROUP`.

    **Expected behaviour:** Command rejected with error message: `"This person already exists in the address book"`.

### Editing a student (`edit`)

1. Editing a student with valid fields

    **Test case:** `edit 1 n/Jane Doe e/janed@u.nus.edu`

    **Expected behaviour:** The student at index 1 has their name updated to "Jane Doe" and email updated. Success message shown: `"Edited Person: Jane Doe; Student ID: ...; Email: janed@u.nus.edu; ..."`.

2. Editing a student with invalid fields

    **Test case:** `edit 1 e/invalid-email-format`

    **Expected behaviour:** Command rejected with an error message indicating the email constraint.

3. Editing a non-existent student

    **Test case:** `edit 999 n/Jane Doe` (where index 999 exceeds the displayed list size)

    **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`.

4. Editing with missing edit fields

    **Test case:** `edit 1` (no fields to edit specified)

    **Expected behaviour:** Command rejected with error message: `"At least one field to edit must be provided."`.


### Viewing Help Window (`help`)

1. Opening the Help Window with `help` command

    **Test case:** Type `help` and press Enter.

    **Expected behaviour:** The Help Window opens. Success message shown: `"Opened help window."`.

2. Opening the Help Window with the keyboard shortcut

    **Test case:** Press the F1 key (or fn + F1 on Mac).

    **Expected behaviour:** The Help Window opens. Success message shown: `"Opened help window."`.

3. Opening the Help Window with extra text after the `help` command

    **Test case:** Type `help icecream` (with extra text after `help`).

    **Expected behaviour:** The Help Window still opens (extra text is ignored). Success message shown: `"Opened help window."`.

4. Window Focus Behavior

    **Prerequisite:** The Help Window is already open but not minimized.

    **Test case:** Type `help` or press F1 again while the main window has focus.

    **Expected behaviour:** The existing Help Window is brought to the front/focus. No duplicate window is created.


### Finding a Student (`find`)

1. Single-keyword search

    **Test case:** Enter `find Alice` where "Alice" exists in sample data.

    **Expected behaviour:** Displayed list shows students whose names contain a word starting with "Alice" (case-insensitive). Success message shown: `"X students listed!"` where X is the number of matching students.

2. Multiple-keyword search

    **Test case:** Enter `find Al Bob` where both keywords match different students.

    **Expected behaviour:** Displayed list contains students matching any of the keywords (OR across keywords). No duplicates. Success message shown: `"X students listed!"` where X is the number of matching students.

3. Case and prefix matching

    **Test case:** Enter `find ann` to match "Annabelle" and `find ANN`.

    **Expected behaviour:** Both commands produce the same results. Matching is case-insensitive and supports prefix matching (e.g., "ann" matches any name word starting with "ann").

4. Empty or whitespace-only query

    **Test case:** Enter `find` with no keywords or only whitespace.

    **Expected behaviour:** Command rejected with error message: `"Find command requires at least one keyword."` followed by the `find` command usage. Displayed list remains unchanged.

5. Invalid special characters

    **Test case:** `find A123` or `find @@@`.

    **Expected behaviour:** Command rejected with error message: `"Keywords should contain alphabetic characters separated by spaces only."` followed by the `find` command usage.

### Filtering the Student List (`filter`)

1. Single criterion filtering

    **Test case:** `filter crs/CS2103T`

    **Expected behaviour:** List displays only students in course CS2103T. Success message shown: `"There are X students matching this filter."` where X is the number of matching students.

2. Multiple criteria filtering

    **Test case:** `filter crs/CS2103T tg/T01 abs/2`

    **Expected behaviour:** List displays only students who satisfy all criteria — course is CS2103T AND tutorial group is T01 AND absence count ≥ 2 (AND logic). Success message shown: `"There are X students matching this filter."`.

3. No matches for filter

    **Test case:** `filter crs/CS9999` (a non-existent course).

    **Expected behaviour:** List becomes empty; Success message shown: `"There are 0 students matching this filter."`.

4. Absence threshold checks

    **Test case:** `filter abs/5`

    **Expected behaviour:** Matches only students with at least 5 absences. Success message shown: `"There are X students matching this filter."`.

5. No filter fields

    **Test case:** `filter` (empty command, no filter criteria).

    **Expected behaviour:** Command rejected with error message: `"At least one filter must be provided."` followed by the filter command usage.

### Deleting a student (`delete`)

1. Deleting a student by index

    **Test case:** `delete 1`

    **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message: `"Are you sure you want to delete <student name>? Type 'yes' to confirm or 'no' to cancel."`.

2. Deleting a student by student details

    **Test case:** `delete id/A1234567X crs/CS2103T tg/T01`

    **Expected behaviour:** If a student matching the given `StudentId`, `CourseId`, and `TGroup` exists in the entire TeachAssist list, TeachAssist does not delete the student immediately. Instead, it shows a confirmation message asking the user to type `yes` to confirm or `no` to cancel.

3. Confirming a deletion

    **Test case:** Enter a valid delete command such as `delete 1`, then enter `yes`.

    **Expected behaviour:** The pending deletion is executed, the student is removed from TeachAssist, and a success message is shown: `"Deleted Person: <student details>"`.

4. Cancelling a deletion

    **Test case:** Enter a valid delete command such as `delete 1`, then enter `no`.

    **Expected behaviour:** The pending deletion is cancelled, no student is removed, and a cancellation message is shown: `"Delete operation cancelled."`.

5. Entering another command while deletion is pending

    **Test case:** Enter a valid delete command such as `delete 1`, then enter another command such as `list`.

    **Expected behaviour:** The pending deletion is cleared and the new command (`list`) is processed normally. No student is deleted unless the user re-enters the delete command and confirms it.

6. Deleting with invalid command format

    **Test case:** `delete abc`

    **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown indicating invalid command format.

7. Deleting with invalid index format

    **Test case:** `delete -1`

    **Expected behaviour:** The command is rejected, no confirmation is requested, and an error message is shown: `"Invalid index number"`.

8. Deleting a non-existent student by details

    **Prerequisite:** Ensure that no student in the address book matches these 3 fields: `id/A0000000Z crs/CS9999 tg/T99`

    **Test case:** `delete id/A0000000Z crs/CS9999 tg/T99`

    **Expected behaviour:** The command is rejected with error message: `"No student matching the given student ID, course ID, and tutorial group was found."`. No confirmation is requested.

### Updating a student's progress (`updateprogress`)

1. Updating progress with a valid status

    **Test case:** `updateprogress 1 p/on_track`

    **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student's progress is updated to `ON_TRACK` and a success message is shown: `"Updated progress for student: <student details>. New progress: ON_TRACK"`. Note: progress values are case-insensitive (`ON_TRACK` and `on_track` both work).

2. Updating progress with an invalid status

    **Test case:** `updateprogress 1 p/GOOD`

    **Expected behaviour:** The command is rejected, no student record is updated, and an error message is shown: `"Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear."`.

3. Updating progress with an invalid index

    **Test case:** `updateprogress 999 p/at_risk`

    **Expected behaviour:** If index `999` is outside the bounds of the current filtered list, the command is rejected, no student record is updated, and an error message is shown: `"The student index provided is invalid"`.

4. Removing progress using `clear` or `not_set`

    **Test case:** `updateprogress 1 p/clear` (or equivalently `updateprogress 1 p/not_set`)

    **Expected behaviour:** If index `1` refers to a valid student in the current filtered list, the student's progress is reset to `NOT_SET`. The progress tag is removed from the student card in the UI, and a success message is shown: `"Cleared progress for student: <student details>"`.

5. Updating progress with invalid command format

    **Test case:** `updateprogress p/on_track` (missing index)

    **Expected behaviour:** The command is rejected because the required student index is missing, no student record is updated, and an error message is shown with the correct usage format.

### Marking a student's attendance (`marka`)

1. Marking attendance with valid input

    **Test case:** `marka 1 wk/3 s/Y`

    **Expected behaviour:** Student at index 1 has week 3 marked as attended. Success message: `"Week 3 marked as Y (Present) for: <student name> (<student ID>)"`.

2. Marking attendance with invalid week

    **Test case:** `marka 1 wk/20 s/Y`

    **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

3. Marking attendance with cancelled week

    **Test case:**
    `cancelw crs/CS2103T tg/T01 wk/3`
    then
    `marka 1 wk/3 s/Y`

    **Expected behaviour:** Command rejected with error message: `"Week 3 is cancelled and cannot be marked."`.

4. Marking attendance duplicate status

    **Test case:**
    `marka 1 wk/2 s/Y`
    then
    `marka 1 wk/2 s/Y`

    **Expected behaviour:** Second command rejected with error message: `"Week 2 already has status 'Y' for <student name> (<student ID>)."`.

5. Marking attendance for non-existent student

    **Test case:** `marka 999 wk/2 s/Y`

    **Expected behaviour:** Command rejected with error message: `"The person index provided is invalid."`.

### Cancelling a week (`cancelw`)

1. Cancelling a week with valid input

    **Test case:** `cancelw crs/CS2103T tg/T01 wk/5`

    **Expected behaviour:** Week 5 cancelled for all students in CS2103T T01. Success message: `"Week 5 cancelled for course CS2103T tutorial T01."`.

2. Cancelling already cancelled week

    **Test case:** Run `cancelw crs/CS2103T tg/T01 wk/5` twice.

    **Expected behaviour:** Second command rejected with error message: `"Week 5 is already cancelled for course CS2103T tutorial T01."`.

3. Cancelling invalid week

    **Test case:** `cancelw crs/CS2103T tg/T01 wk/20`

    **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

4. Cancelling non-existent course/tutorial

    **Test case:** `cancelw crs/CS9999 tg/T99 wk/2`

    **Expected behaviour:** Command rejected with error message: `"Course CS9999 with tutorial T99 does not exist and cannot be cancelled."`.

### Uncancelling a week (`uncancelw`)

1. Uncancelling valid week

    **Test case:**
    `cancelw crs/CS2103T tg/T01 wk/4`
    then
    `uncancelw crs/CS2103T tg/T01 wk/4`

    **Expected behaviour:** Week 4 restored for all students. Success message: `"Week 4 uncancelled for course CS2103T tutorial T01."`.

2. Uncancelling non-cancelled week

    **Test case:** `uncancelw crs/CS2103T tg/T01 wk/3` (where week 3 has not been cancelled)

    **Expected behaviour:** Command rejected with error message: `"Week 3 is not cancelled for course CS2103T tutorial T01."`.

3. Uncancelling invalid week

    **Test case:** `uncancelw crs/CS2103T tg/T01 wk/20`

    **Expected behaviour:** Command rejected with error message: `"Invalid week number. Valid range: 1 to 13."`.

4. Uncancelling non-existent course/tutorial

    **Test case:** `uncancelw crs/CS9999 tg/T99 wk/1`

    **Expected behaviour:** Command rejected with error message: `"Course CS9999 with tutorial T99 does not exist and cannot be uncancelled."`.

### Adding a remark (`remark`)

1. Adding a remark with valid input

    **Test case:** `remark 1 txt/Participates actively in class`

    **Expected behaviour:** A remark with the text "Participates actively in class" and the current date is added to the student at index 1. Success message shown: 
    ```
    Added remark to Person:
    <student details>
    Remark: Participates actively in class
    ```

2. Adding a remark with missing text

    **Test case:** `remark 1 txt/`

    **Expected behaviour:** Command rejected with error message: `"Remark text cannot be empty."`.

3. Adding a remark with missing prefix

    **Test case:** `remark 1`

    **Expected behaviour:** Command rejected with an error message showing the correct usage format.

4. Adding a remark with invalid index

    **Test case:** `remark 999 txt/Some remark` (where index 999 exceeds the displayed list size)

    **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`.

### Viewing student details / remarks (`view`)

1. Viewing a student with valid input

    **Test case:** Ensure a student is visible in the current displayed list, then enter `view 1` and press Enter.

    **Expected behaviour:** The detail pane displays the selected student's full information (name, student ID, course, tutorial group, email, tele), attendance summary and remark entries. Success message shown: `"Viewing student: <student details>"`.

2. Viewing a non-existent / out-of-range index

    **Test case:** Enter `view 9999` (index greater than displayed list size) or `view 0`.

    **Expected behaviour:** Command rejected with error message: `"The student index provided is invalid"`; detail pane remains unchanged.


### Listing students (`list`)

1. Listing all students

    **Test case:** First apply a filter (e.g., `filter crs/CS2103T`), then enter `list`.

    **Expected behaviour:** The full student list is displayed (all filters cleared), sorted alphabetically by name. Success message shown: `"Listed all persons"`.

### Clearing the student list / sample data (`clear`)

1. Clearing all students

    **Test case:** Ensure the list has at least one student, then enter `clear`.

    **Expected behaviour:** All student records are removed from TeachAssist. The list becomes empty. Success message shown: `"Address book has been cleared!"`.

2. Clearing when the list is already empty

    **Test case:** Enter `clear` when the student list is already empty (e.g., after a previous `clear`).

    **Expected behaviour:** The command succeeds with the same message: `"Address book has been cleared!"`. No error is shown.

### Clearing filters (`list`)

Note: There is no dedicated `clearfilter` command. To reset any active filter and return to the full student list, use the `list` command.

1. Clearing an active filter

    **Test case:** First apply a filter (e.g., `filter crs/CS2103T`), verify the list is filtered, then enter `list`.

    **Expected behaviour:** The full student list is restored, sorted alphabetically by name. All filters are cleared. Success message shown: `"Listed all persons"`.

2. Clearing when no filter is active

    **Test case:** Without any active filter, enter `list`.

    **Expected behaviour:** The full student list is displayed (unchanged). Success message shown: `"Listed all persons"`. No error is shown.

### Saving data

1. Data persistence after normal usage

    **Test case:** Add a student (e.g., `add n/Test Student id/A9999999Z e/test@u.nus.edu crs/CS2103T tg/T01`), then close the app using `exit` and relaunch it.

    **Expected behaviour:** The newly added student appears in the list after relaunching. All data modifications (adds, edits, deletes) are persisted to `data/addressbook.json`.

2. Dealing with missing data files

    **Test case:** Close the app, delete the `data/addressbook.json` file, then relaunch the app.

    **Expected behaviour:** The app launches with the default sample data, as if running for the first time. A new `data/addressbook.json` file is created upon the next data-modifying command.

3. Dealing with corrupted data files

    **Test case:** Close the app, open `data/addressbook.json` in a text editor and corrupt it (e.g., delete a closing brace or add invalid characters), then relaunch the app.

    **Expected behaviour:** The app launches with an empty student list. The corrupted data file is not loaded. A warning may be logged.

### Suggested exploratory testing

1. Combining multiple commands in sequence

    1. **Workflow:** Add a student → mark their attendance for weeks 1–3 → update their progress to `at_risk` → add a remark → use `view` to verify all data → edit their email → use `view` again to confirm the edit is reflected → delete the student with confirmation. Verify each step produces the correct success message and the data is consistent throughout.

2. Testing invalid inputs and edge cases

    1. **Workflow:** Try each command with: empty arguments, extra spaces, very long input strings, special characters in fields, index `0`, negative indices, and indices exceeding the list size. Verify that all invalid inputs produce clear, specific error messages and do not corrupt the application state.

3. Testing persistence across restarts

    1. **Workflow:** Make several data changes (add students, mark attendance, update progress, add remarks), then close the app with `exit`. Relaunch and verify that all changes persist. Then close without `exit` (e.g., close the window) and verify data is still saved.


## **Appendix: Planned Enhancements**

1. Relax student name and find command keywords validation to support special characters. Currently, the name field accepts only alphabetical characters and spaces; we plan to extend this to support names containing hyphens, apostrophes, and other common punctuation, such as “O’Connor” and “Smith-Jones.”

2. Extend find to support prefix-based search across additional fields such as student ID, email, and course, instead of names only.

3. Add support for multi-value filtering. Currently, each filter prefix accepts only a single value; we plan to extend this to allow multiple values under the same prefix in a single filter command.

4. Add support for more flexible absence filtering. Currently, absence filtering only supports values greater than or equal to a given threshold; we plan to extend this to support exact values, upper bounds, and ranges.

5. Add confirmation support for `clear`. Currently, `clear` removes all student records immediately after execution. We plan to introduce an optional confirmation workflow similar to `delete`, so that users must explicitly confirm before all records are removed. One possible implementation is to let `LogicManager` temporarily store a pending clear action after a valid `clear` command is entered, and only execute the actual clearing when the user responds with `yes`. Entering `no` or another command would cancel the pending clear action. This would reduce the risk of accidental mass deletion while keeping the command behaviour consistent with other destructive operations.

6. Add support for multi-remark deletion. Currently, when multiple `r/` prefixes are entered, only the last `r/` prefix is taken; we plan to extend this to allow multiple remark indices to be chosen for deletion under a single `unremark` command.
