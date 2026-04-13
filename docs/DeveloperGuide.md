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

- **GitHub Copilot** — used as a code-completion assistant throughout the project. Copilot suggestions were used to accelerate writing of routine boilerplate code, test cases, and JavaDoc comments. It was also used to help identify and extract duplicated logic into shared utility methods and common helper classes across command and parser implementations, reducing code duplication. AI was also used to refine the wording and clarity of the User Guide and Developer Guide. When troubleshooting and debugging, Copilot was used as a tool to help locate and diagnose problems. All AI-generated suggestions were reviewed, tested, and adapted by the team before being committed.
- **ChatGPT (by OpenAI)** — used to check for code quality issues such as incorrect SLAP (Single Level of Abstraction Principle) violations. All suggestions were reviewed and validated by the team before being applied.

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
* keeps the `ViewWindow` in sync with the displayed data, updating or clearing it as needed after each command.


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


#### Technical Overview
The `filter` command is implemented using the `FilterMatchesPredicate` class, which encapsulates all supported filtering criteria as optional fields. This predicate is passed to `Model#updateFilteredPersonList(Predicate)` to update the observable list of students. The command does not mutate any underlying data, only the filtered view.

**Supported Criteria:**
- Course ID (`crs/`)
- Tutorial Group (`tg/`)
- Progress Status (`p/`)
- Minimum Absence Count (`abs/`)

All criteria are combined using logical AND; a student must satisfy all present filters to be included. If no criteria are provided, the command is rejected at the parser level.

#### Implementation Details
`FilterMatchesPredicate` implements `Predicate<Person>`, storing each criterion as an `Optional`. The `test(Person)` method evaluates all present criteria:
- Course ID and Tutorial Group: case-insensitive exact match
- Progress Status: exact enum match (`Progress` enum)
- Absence Count: threshold match (≥)

The predicate is constructed in `FilterCommandParser` and passed to the model. The UI observes the filtered list and updates automatically.

#### Design Logic
**Predicate Composition:**
The design uses a single predicate class (`FilterMatchesPredicate`) with all logic centralized. This approach was chosen over functional composition (e.g., chaining with `predicate.and()`) to simplify debugging, extension, and maintenance. Adding a new filter only requires adding a field and logic to the predicate, rather than composing multiple smaller predicates. While functional composition offers modularity, it increases complexity and can obscure the overall filtering logic.

**Pros:**
- Centralized logic improves maintainability and extensibility.
- Defensive copying of criteria prevents accidental mutation.
- Predicate is easily serializable for testing.

**Cons:**
- Less flexible for OR-based or more complex queries.
- Slightly increased coupling between filter logic and predicate structure.

**Scalability:**
The single-predicate approach scales well for AND-based filtering but would require refactoring for more advanced query logic (e.g., OR, NOT).


### Feature: Delete Student


#### Technical Overview
The `delete` feature is implemented as a two-stage confirmation workflow to prevent accidental data loss. Deletion can be specified by either the displayed index or by the tuple (`STUDENT_ID`, `COURSE_ID`, `TUTORIAL_GROUP`).

#### Implementation and State Management
1. **Command Parsing:**
   - `DeleteCommandParser` creates a `DeleteCommand` instance, resolving the target student but not deleting immediately.
2. **Pending Confirmation:**
   - `LogicManager` calls `DeleteCommand#getConfirmedCommand(Model)` to create a `ConfirmedDeleteCommand` and stores it as a pending command.
   - The confirmation prompt is generated by `DeleteCommand#getConfirmationMessage(Model)`.
3. **User Response Handling:**
   - If the next command is `yes`, `LogicManager` executes the stored `ConfirmedDeleteCommand` via `execute(Model)`, which calls `Model#deletePerson(Person)`.
   - If the next command is anything else, the pending command is cleared and the new command is processed; no deletion occurs.
4. **State Management:**
   - The pending confirmation is transient and only valid for the immediate next command. This is enforced by `LogicManager`, which clears the pending command on any non-`yes`/`no` input.

**Key Classes and Methods:**
- `DeleteCommand#getConfirmedCommand(Model)`
- `DeleteCommand#getConfirmationMessage(Model)`
- `LogicManager` (pending command storage and routing)
- `ConfirmedDeleteCommand#execute(Model)`

<box type="info" seamless>
**Relevant diagram:** Delete confirmation workflow.
<puml src="diagrams/DeleteConfirmationActivityDiagram.puml" width="600" />
</box>

#### Design Logic
**Two-Stage Workflow:**
This design decouples command parsing from execution, reducing the risk of accidental destructive actions. Pending state is managed in `LogicManager`, not the model, ensuring that only one destructive action can be pending at a time.

**Pros:**
- Strong safety against accidental deletion (immutability of model until explicit confirmation).
- Low coupling between command parsing and execution.
- Defensive clearing of pending state on unrelated commands.

**Cons:**
- Slightly increased complexity in `LogicManager`.
- Pending state is not persisted; lost on restart.

**Scalability:**
The pattern can be extended to other destructive commands (e.g., `clear`) with minimal changes.

### Feature: Update Progress


#### Technical Overview
The `updateprogress` command updates a student's progress status, which is represented by the `Progress` enum in the model. The enum values are:
- `ON_TRACK`
- `NEEDS_ATTENTION`
- `AT_RISK`
- `NOT_SET` (default)

#### Implementation Details
The `Progress` enum enforces valid states and prevents inconsistent or invalid progress values. The `NOT_SET` state is used as a sentinel value to indicate the absence of an explicit status. The UI layer maps each enum value to a color for display; when the value is `NOT_SET`, no label is rendered, keeping the UI uncluttered.

#### Design Logic

**NOT_SET State:**
The `NOT_SET` state is not shown in the UI, reducing visual clutter. This separation of model and UI logic allows the model to remain expressive while the UI remains clean.

### Feature: Mark Attendance Command

#### Overview

The `marka` command allows tutors to record or update a student’s attendance for a specific week. This feature enables per-week attendance tracking instead of aggregate counts, providing finer control over tutorial participation records.

##### Attendance Fields
Status: This represents the attendance status of a student.
- `Y` → Present
- `A` → Absent
- `N` → Not marked
Week Number: This represents the week in a regular NUS semester and ranges from (1 to 13)

##### Command
The command targets a student by their displayed index in the current person list and applies an attendance status to a specified week. The command word is `marka`, and its expected format is:

`marka INDEX wk/WEEK_NUMBER s/STATUS`

For example, `marka 1 wk/3 s/Y` marks week 3 as attended for the first student in the displayed list.

#### Attendance representation

Attendance is modeled using `Week` and `WeekList`.

Each `Week` object represents the attendance state for a specific week and encapsulates:
- the week number
- the attendance status (`Y`, `A`, or `N`)
- whether the week is cancelled (`C`)

Each `Person` contains a `WeekList`, which represents attendance across all weeks.

- `WeekList` is stored as part of each `Person`
- Each `Week` enforces its own state constraints
- Updates to attendance are performed through controlled methods such as:
  - `markWeekAsAttended`
  - `markWeekAsAbsent`
  - `markWeekAsDefault`

This design ensures that attendance rules are enforced consistently at the model level.

#### Implementation

When the user enters a `marka` command, it is parsed into a `MarkAttendanceCommand`.

After successful parsing, `MarkAttendanceCommand#execute` is invoked to perform the update.

When `MarkAttendanceCommand#execute` is called, the command retrieves the currently filtered person list from the model and checks whether the provided index is within bounds. If the index is invalid, a `CommandException` is thrown.

Next, the command validates that the specified week number falls within the allowed range. It then retrieves the target `Person` and creates a defensive copy of the person’s `WeekList` to preserve immutability.

Before applying any update, the command checks whether the selected week has been marked as cancelled. If the week is cancelled, the operation is rejected.

The attendance status is then updated according to the specified status.

If the requested status is already set, the command rejects the operation to prevent redundant updates.

After the update, a new `Person` object is created with the modified `WeekList`, and the model is updated using `model.setPerson(personToEdit, editedPerson)`. A success message is then returned to the user.

An important implementation detail is that the command does not mutate the original `Person` or `WeekList` directly. Instead, it operates on a copied `WeekList` and replaces the original `Person` in the model. This keeps updates explicit and consistent with the application’s design.

#### Key Behaviours

- **Strict index validation**  
  Invalid student indices are rejected.

- **Week boundary validation**  
  Only valid week numbers are accepted.

- **Cancelled week protection**  
  Attendance cannot be modified for cancelled weeks.

- **Duplicate state protection**  
  Reapplying the same attendance status is not allowed.

- **Immutability**  
  Updates are performed on copies, and the modified student replaces the original in the model.

#### Implementation Details
**Copy-on-Write Strategy:**
To preserve the immutability of `Person` objects, the `WeekList` is duplicated before any update. The command creates a defensive copy of the `WeekList`, applies the attendance update, and constructs a new `Person` instance with the updated list. This prevents unintended side effects and ensures that all references remain consistent.

**Business Rule Enforcement:**
Validation (e.g., for cancelled weeks) is performed at the command layer before updating the model. This provides immediate feedback and prevents invalid state transitions from reaching the model.

#### Design Logic
**Pros:**
- Immutability of `Person` objects prevents shared-state bugs.
- Defensive copying ensures thread safety and predictable UI updates.
- Validation at the command layer improves transparency and debuggability.

**Cons:**
- Slight performance overhead due to object copying.
- Some duplication of validation logic between command and model layers.

**Scalability:**
The copy-on-write approach is robust for moderate data sizes and aligns with functional programming best practices.

**Aspect: Responsibility separation**
* **Command:** Handles semantic validation and business rules
* **Model:** Performs state updates only after validation
* This separation ensures clear layering and maintainability of the system.

#### Sequence diagram

The following diagram shows how attendance input is parsed, validated, and applied to the target student.
<puml src="diagrams/MarkAttendanceSequenceDiagram.puml" width="600" />

### Feature: Cancel Week Command

#### Overview

The `cancelw` command allows a teaching assistant to mark a specific week as cancelled for all students within a given course and tutorial group. This is useful for handling situations such as public holidays or cancelled classes.

A cancelled week has the following properties:
- It cannot be marked for attendance
- It is excluded from attendance-related calculations

The command word is `cancelw`, and its expected format is:

`cancelw crs/COURSE_ID tg/TUTORIAL_GROUP wk/WEEK_NUMBER`

For example, `cancelw crs/CS2103T tg/T01 wk/5` marks week 5 as cancelled for all students in course CS2103T and tutorial group T01.

#### Cancellation representation

Cancellation state is managed using both a centralized structure and per-student data.

At the model level:
- Cancelled weeks are tracked using a `cancelledWeeksMap` in `ModelManager`
- The key is a combination of course ID and tutorial group
- The value is a set of cancelled week indices

At the individual level:
- Each `Person` contains a `WeekList`
- Each `Week` stores whether it has been cancelled
- When a week is cancelled, its previous attendance state is preserved internally

This dual-layer design ensures both efficient lookup and consistent propagation of cancellation state.

#### Implementation

When the user enters a `cancelw` command, it is parsed into `CancelWeekCommand`.

After successful parsing, `CancelWeekCommand#execute` is invoked to perform the update.

When `CancelWeekCommand#execute` is called, the command first validates that the specified course and tutorial group exist in the model. If not, a `CommandException` is thrown.

Next, the command validates that the provided week number falls within the allowed range. It also checks whether the week has already been marked as cancelled. If the week is already cancelled, the operation is rejected.

After validation, the command delegates the update to the model via `model.addCancelledWeek(courseId, tGroup, weekIndex)`.

#### Model-Level Logic

`ModelManager#addCancelledWeek` handles the core cancellation logic.

It first constructs a key using the course ID and tutorial group, then retrieves or initializes the corresponding set of cancelled weeks. The specified week index is added to this set.

The cancellation is then propagated to all students belonging to the same course and tutorial group:
- each affected student is updated using the same copy-and-replace pattern described in the Mark Attendance feature

Finally, the updated cancellation state is persisted to the address book.

#### Key Behaviours

- **Strict validation**  
  Invalid course–tutorial group combinations are rejected, and attempting to cancel an already cancelled week results in an error.

- **Batch update**  
  Cancellation is applied consistently across all students in the same course and tutorial group.

- **State preservation**  
  Any existing attendance status for the week is retained internally within the `Week` object.

- **Immutability**  
  Updates are performed on copies of `WeekList`, and modified `Person` objects replace the originals in the model.

#### Design Logic
**Pros:**
- Centralized state enables efficient queries and consistent updates.
- Defensive propagation prevents stale or inconsistent state.

**Cons:**
- Requires careful synchronization between the map and each `WeekList`.
- Slightly increased coupling between group-level and student-level data.

**Scalability:**
Centralized mapping is efficient for large cohorts and supports future extensions (e.g., batch operations).

---


### Feature: Uncancel Week Command

#### Overview

The `uncancelw` command allows a teaching assistant to reverse a previously cancelled week for a given course and tutorial group. This is useful when a cancelled class is reinstated and attendance tracking needs to resume.

After uncancelling:
- The week becomes available for attendance marking
- The previously stored attendance status is restored (within the same session)

**Format:**
uncancelw crs/COURSE_ID tg/TUTORIAL_GROUP wk/WEEK_NUMBER

For example, `uncancelw crs/CS2103T tg/T01 wk/5` restores week 5 for all students in course CS2103T and tutorial group T01.

#### Cancellation reversal representation

Uncancellation builds on the same `Week` and `WeekList` structures described in the Mark Attendance feature.

<box type="info" seamless></box>

**Note:** Transience of Previous Week Status

The `prevStatus` field in `Week` is transient and not persisted to storage. It is only used during runtime to support uncancellation within the same session.

This means:
- If a week is uncancelled within the same session, its previous attendance state is restored
- If the application is restarted, uncancelling restores the week to its default state

This design avoids persisting temporary state and keeps storage simpler.
</box>

#### Implementation

When the user enters a `uncancelw` command, it is parsed into `UnCancelWeekCommand`.

After successful parsing, `UnCancelWeekCommand#execute` is invoked to perform the update.

When `UnCancelWeekCommand#execute` is called, the command validates that the specified course–tutorial group exists and that the week number is within the valid range.

It then checks whether the specified week is currently cancelled. If the week is not cancelled, the operation is rejected.

After validation, the command delegates the update to the model via `model.removeCancelledWeek(courseId, tGroup, weekIndex)`.

#### Model-Level Logic

`ModelManager#removeCancelledWeek` performs the uncancellation.

It locates the corresponding entry in `cancelledWeeksMap` and removes the specified week index.

The change is then propagated to all students in the same course and tutorial group:
- updates follow the same copy-and-replace pattern described earlier, with the week restored to its previous state

Finally, the updated state is persisted.

#### Key Behaviours

- **Strict validation**  
  Only cancelled weeks can be uncancelled; invalid operations are rejected.

- **State restoration**  
  The original attendance status is restored when available within the same session.

- **Batch update**  
  All affected students are updated consistently.

- **Immutability**  
  Updates are applied on copied `WeekList` instances and reflected via person replacement.


#### Design Considerations

**Aspect: Restoring previous attendance state**

* **Current choice - Store previous status inside Week:** This ensures accurate restoration of original attendance and preserves user input history. However, this introduces additional state management complexity.
* **Alternative - Reset to default status:** This requires simpler implementation but causes loss of original attendance information.

**Scalability:**
The approach is robust for session-based workflows and avoids long-term state bloat.

### Feature: Remark Command

#### Technical Overview
The `remark` command attaches a remark to a student. Remarks are represented as `Remark` objects, each storing the remark text and the creation date. The creation date is assigned using `LocalDate.now()` at the parser or command level, ensuring that the model remains deterministic and testable.

#### Implementation Details
- Remarks are stored in each `Person` as a `List<Remark>`.
- In storage, `JsonAdaptedPerson` serializes remarks as a `List<JsonAdaptedRemark>`, each with `text` and `date` fields.
- During deserialization, each `JsonAdaptedRemark` is converted back to a model-level `Remark` and reattached to the corresponding person.

**Serialization:**
The serialization process ensures that all metadata is preserved. The use of `JsonAdaptedRemark` decouples the storage format from the model, supporting defensive copying and future extensibility.

**Date Handling:**
`LocalDate.now()` is invoked at the parser or command level, not in the model, to ensure that the model remains pure and testable. This also allows for easier testing and deterministic behavior.

**Copy-on-Write:**
The command does not mutate the original `Person` object. Instead, it creates a copy, adds the remark, and updates the model using `setPerson(...)`. This aligns with the application's immutability pattern.


![Remark command sequence diagram](images/RemarkSequenceDiagram.puml)

### Feature: View Command


#### Technical Overview
The `view` command displays a student's full details in a dedicated panel (`ViewWindow`). The feature is implemented by `ViewCommand`, `ViewCommandParser`, the `ViewWindow` UI component, and auto-sync logic in `MainWindow`.

#### Implementation Details
1. **`ViewCommand`**: Retrieves the `Person` from the model's filtered list and returns a `CommandResult` with a reference to the person. `CommandResult#shouldShowView()` signals the UI to display the view panel.
2. **`MainWindow#handleCommandResult`**: If `shouldShowView()` is true, calls `handleView(person)`, which passes the person to `ViewWindow#setPerson` and ensures the panel is visible.
3. **`ViewWindow#setPerson`**: Populates UI fields and dynamically generates remark entries.
4. **Click-to-view**: Mouse click handlers in `PersonListPanel` invoke the same logic, ensuring consistent behavior.

#### Auto-Sync Logic
After every command, `MainWindow#updateViewWindowAfterCommand()` ensures the view panel remains in sync with the underlying data:
1. If the panel is not visible, do nothing.
2. Otherwise, search the filtered list for the student being viewed using `ViewWindow#isViewing(Person)`, which uses `Person#isSamePerson` (name + course + tutorial group) for comparison. This allows the view to persist even if identity fields are edited.
3. If found, refresh the panel with the updated person; if not, clear the panel and selection.


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



### \[Proposed\] Undo / Redo

#### Motivation

Currently, TeachAssist has no way to reverse a command after execution. A TA who accidentally deletes the wrong student, overwrites attendance, or edits the wrong field must manually re-enter the correct data. An undo/redo mechanism would let users recover from mistakes instantly.

#### Proposed implementation

The feature centres on a `VersionedAddressBook` that extends `AddressBook` with an internal state history list and a `currentStatePointer`. Each mutating command (e.g., `add`, `edit`, `delete`, `marka`) calls `Model#commitAddressBook()` after execution, which saves a copy of the current address book state to the history.

- `UndoCommand` calls `Model#undoAddressBook()`, which decrements the pointer and restores the previous state.
- `RedoCommand` calls `Model#redoAddressBook()`, which increments the pointer and restores the next state.
- If a new mutating command is executed after an undo, all forward states beyond the pointer are discarded.

#### Design considerations

**Aspect: State storage granularity**

* **Chosen approach — Full address book snapshots:** Each commit stores a complete copy of the address book. Simple to implement and reason about, but uses more memory for large datasets.
* **Alternative — Command-level inverse operations:** Store the inverse of each command (e.g., an `add` stores a corresponding `delete`). More memory-efficient, but significantly harder to implement correctly for commands that modify multiple records (e.g., `cancelw` affecting all students in a group).


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

TeachAssist is a desktop CLI application for university teaching assistants at NUS to manage student data. It supports structured storage of student records, attendance tracking, progress tagging, and remarks. It is a local, offline tool — it does not integrate with external platforms, compute grades, or provide automated analytics.

**Target user profile**:

* Full time University Teaching Assistants (TAs) at NUS
* Manages student contacts and records across multiple courses and tutorial groups
* Prefers desktop applications over web-based tools
* Comfortable with CLI applications and fast at typing

**Value proposition**:
TeachAssist consolidates student data, attendance, progress tracking, and consultation remarks into a single application, eliminating the need to juggle multiple platforms and spreadsheets. Its typing-based command interface lets TAs perform these tasks faster than a typical mouse-driven application.


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
**Main Success Scenario:**
1. User requests assistance or information about using the application.
2. TeachAssist displays a help window summarizing available features and provides a link to the user guide.
3. Use case ends.

**Extensions:**

* 1a. The help window is already open but not minimized.
    * 1a1. TeachAssist brings the existing help window to the front.
    * Use case ends.


**Use Case: UC02 – Add Student**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User wants to add a student and provides the required details.
2. TeachAssist validates the input and required fields.
3. TeachAssist adds the student to the list.
4. TeachAssist confirms the addition.
5. Use case ends.

**Extensions:**

* 2a. The input is invalid or incomplete.
    * 2a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 2b. The student would duplicate an existing record (same identifying fields).
    * 2b1. TeachAssist informs the user that the student already exists.
    * Use case ends.


**Use Case: UC03 – Find Students by Name**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User provides one or more keywords to search for students by name.
2. TeachAssist searches for and displays only matching students.
3. TeachAssist shows the number of students found.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or empty.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 2a. No students match the keywords.
    * 2a1. TeachAssist displays an empty list and indicates no matches.
    * Use case ends.


**Use Case: UC04 – Filter Student List**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User specifies one or more criteria to filter the student list (e.g., by course, group, progress, or absences).
2. TeachAssist applies the filter and displays only matching students.
3. TeachAssist shows the number of students matching the filter.
4. Use case ends.

**Extensions:**

* 1a. No criteria are provided.
    * 1a1. TeachAssist displays an error and requests at least one filter.
    * Use case ends.
* 1b. Criteria are missing values or are invalid.
    * 1b1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 2a. No students match the filter.
    * 2a1. TeachAssist displays an empty list and indicates no matches.
    * Use case ends.


**Use Case: UC05 – Edit Student**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User wnats to edit a student's details.
2. TeachAssist updates the student record accordingly.
3. TeachAssist confirms the update.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student could not be found.
    * Use case ends.


**Use Case: UC06 – Mark Student Attendance**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User specifies a student, a week, and an attendance status to record attendance.
2. TeachAssist updates the attendance record for the specified week.
3. TeachAssist confirms the update.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student could not be found.
    * Use case ends.
* 1c. The specified week is invalid or cancelled.
    * 1c1. TeachAssist informs the user that the week cannot be marked.
    * Use case ends.
* 1d. The attendance status is invalid or already set.
    * 1d1. TeachAssist informs the user of valid statuses or that the status is already assigned.
    * Use case ends.


**Use Case: UC07 – Cancel Tutorial Week**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User requests to cancel a specific week for a course and tutorial group.
2. TeachAssist marks the week as cancelled for all students in the group.
3. TeachAssist confirms the cancellation.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The course or tutorial group does not exist.
    * 1b1. TeachAssist informs the user that the group cannot be found.
    * Use case ends.
* 1c. The week is invalid or already cancelled.
    * 1c1. TeachAssist informs the user that the week cannot be cancelled.
    * Use case ends.


**Use Case: UC08 – Uncancel Tutorial Week**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User requests to restore a previously cancelled week for a course and tutorial group.
2. TeachAssist restores the week to active status for all students in the group.
3. TeachAssist confirms the restoration.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The course or tutorial group does not exist.
    * 1b1. TeachAssist informs the user that the group cannot be found.
    * Use case ends.
* 1c. The week is invalid or was not cancelled.
    * 1c1. TeachAssist informs the user that the week cannot be restored.
    * Use case ends.


**Use Case: UC09 – Update Student Progress Status**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User wants to update a student's progress status and provides the new status.
2. TeachAssist updates the student’s progress status.
3. TeachAssist confirms the update.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student could not be found.
    * Use case ends.
* 1c. The progress status is invalid.
    * 1c1. TeachAssist informs the user of valid statuses.
    * Use case ends.


**Use Case: UC10 – Add Remark to Student**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User wants to add a remark to a student and provides the remark text.
2. TeachAssist adds the remark (with the current date) to the student’s record.
3. TeachAssist confirms the addition.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid, empty, or too long.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.


**Use Case: UC11 – Delete Remark from Student**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User wants to delete a remark from a student and specifies which remark to delete.
2. TeachAssist removes the remark from the student’s record.
3. TeachAssist confirms the deletion.
4. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student could not be found.
    * Use case ends.
* 1c. The specified remark does not exist.
    * 1c1. TeachAssist informs the user that the remark could not be found.
    * Use case ends.


**Use Case: UC12 – View Student Details**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User selects a student to view full details and remarks.
2. TeachAssist displays the student’s details and remarks in a dedicated view.
3. TeachAssist highlights the selected student in the list.
4. TeachAssist confirms which student is being viewed.
5. Use case ends.

**Extensions:**

* 1a. The selection is invalid or out of range.
    * 1a1. TeachAssist displays an error and does not change the view.
    * Use case ends.


**Use Case: UC13 – Delete Student**<br>
**Actor:** User<br>
**Main Success Scenario:**
1. User requests to remove a student from the system.
2. TeachAssist displays the student’s details and asks for confirmation.
3. User confirms the deletion.
4. TeachAssist deletes the student record.
5. Use case ends.

**Extensions:**

* 1a. The input is invalid or incomplete.
    * 1a1. TeachAssist displays an error and requests correction.
    * Use case ends.
* 1b. The specified student does not exist.
    * 1b1. TeachAssist informs the user that the student could not be found.
    * Use case ends.
* 3a. The user cancels the deletion or enters another command.
    * 3a1. TeachAssist cancels the pending deletion and processes the new input if applicable.
    * Use case ends.


### Non-Functional Requirements

- The system should respond to user commands within 1 second under normal usage.
- The system should work on any mainstream operating system (Windows, macOS, Linux) as long as Java 17 or above is installed.
- Users should be able to run the application by executing a JAR file, without needing to run an installer.
- A user with above-average typing speed for regular English text should be able to accomplish most tasks faster using commands than using the mouse.
- Student records must be saved to persistent storage.
- Data should remain available after the system is restarted.
- User data should not be lost due to unexpected situations (e.g., unexpected shutdowns).
- Data persistence should not depend on an external database system; storage must be file-based and embedded within the application.
    
### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **TGroup**: Tutorial Group. Represents a specific tutorial section or group that a student belongs to in the course.
* **Tele**: Telegram username. Used to identify a student by their Telegram handle (e.g., @arielchua).
* **Week**: Refers to a specific teaching week in the NUS academic semester
* **Remark**: A note or comment attached to a student record, used for providing additional information or feedback.
* **TA (Teaching Assistant)**: A person who assists the instructor in teaching, grading, and supporting students in the course.

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

    **Expected behaviour:** Command rejected with an error message: `"Remark text cannot be empty."`.

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

7. Prevent the cursor from changing at the divider between the person list panel and the view panel. Currently, the cursor may suggest that the divider is draggable, which can create confusion if the UI is intended to remain fixed. We plan to make this interaction clearer by keeping the cursor static at the divider.
