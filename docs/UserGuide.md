---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TeachAssist User Guide

TeachAssist is a desktop app designed to help full-time teaching assistants manage student records efficiently. It is optimized for use via a Command Line Interface (CLI), while still offering the benefits of a Graphical User Interface (GUI). For teaching assistants who are comfortable typing, TeachAssist makes common administrative tasks such as adding, finding, filtering, and deleting student records faster and more convenient than traditional point-and-click apps.

---

## Table of contents
- [Quick Start](#quick-start)
- [Features](#features)
  - [Viewing help: `help`](#help)
  - [Adding a student: `add`]
  - [Listing all students `list`]
  - [Deleting a student: `delete`](#delete)
    - [Delete by index](#deletebyindex)
    - [Delete by student details](#deletebydetails)
  - [Finding a student: `find`](#finding-students-by-name-find)
  - [Filtering students: `filter`]
    - [Filter by __]
    - [Filter by __]
  - [Editing a student: `edit`]
  - [Updating a student's progress: `updateprogress`]
  - [Marking a student's attendance: `markattendance`]
  - [Clearing list](#clear)
  - [Adding a remark: `remark`]
  - [Deleting a remark: `unremark`]
  - [Exiting the app](#exit)
- [Command Summary](#command-summary)
- [Parameter Summary](#parameter-summary)
- [FAQ](#faq)

---
## Quick start

1. Ensure you have **Java 17** or above install on your computer.<br>
> **Checking your Java version**
> - Open a command terminal on your computer.
> - Type `java -version` and press Enter.
> - If Java is installed, you will be shown the version number (e.g. `java version 17.0.1`).
> - The first number should be 17 or higher.
>
> **If Java is not installed, or the version number is below 17:**
> - Download and install Java 17 by following the guide:
>   - [for Windows users](https://se-education.org/guides/tutorials/javaInstallationWindows.html) [for Mac users](https://se-education.org/guides/tutorials/javaInstallationMac.html) [for Linux users](https://se-education.org/guides/tutorials/javaInstallationLinux.html)
> - After installation, restart your terminal and check that the correct version has been installed.

2. Download the latest `TeachAssist.jar` file from [here](https://github.com/AY2526S2-CS2103T-F10-3/tp/releases/tag/v1.3)
3. Copy the `TeachAssist.jar` file to the folder you want to use as the _home folder_ for your LambdaLab.
4. Open the command terminal again and do the following:
   - Type `cd name-of-your-home-folder` and press Enter.
   - Type `java -jar TeachAssist.jar` and press Enter to run the application.
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing `help` and pressing Enter will open the help window.<br>
   Some example commands you can try:
   - `help` : Shows the help window that explains the command usage.
   - `list` : Lists all students.
   - `delete 3`: Deletes the student at the current list's index 3.
   - `add n/John Doe id/A0123456X e/johnd@u.nus.edu.com crs/CS2103T tg/T01 tel/@johndoe`: Adds a student named `John Doe`.
   - `clear`: Deletes all students.
   - `exit`: Exits the app.
   
6. Refer to the [Features](#features) below for details of each command.

---

## Features

<a name="add"></a>
### Add a student: `add`

Adds a student to TeachAssist

Format:
```
add n/NAME id/ID e/EMAIL crs/COURSE tg/TGROUP [tel/TELE]
```

<a name="help"></a>
### Viewing help : `help` 

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: 
```
help
```

<a name="delete"></a>

<a name="list"></a>
### Listing all students `list`

Shows all Persons stored sorted in Ascending order

Format:
```
list
```

<a name="delete"></a>
### Deleting a student : `delete`

Removes a student from TeachAssist.

<a name="deletebyindex"></a>
**Delete by index**

Format: 
```
delete INDEX
```

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the currently displayed student list.
* The index **must be a positive integer** 1, 2, 3, …

<a name="deletebydetails"></a>
**Delete by student details**

Format: 
```
delete id/STUDENT_ID crs/COURSE_ID tg/TUTORIAL_GROUP
```

* Deletes the student with the exact details match for `STUDENT_ID`, `COURSE_ID`, and `TUTORIAL_GROUP`.

**Confirmation prompt**

After entering a valid `delete` command, TeachAssist will show a confirmation pop-up.<br>
Enter `yes` to proceed with the deletion, or `no` to cancel it.

**Examples**:

`delete 1` followed by `yes`
* Deletes the 1st student in the currently displayed student list.

`delete id/A1234567X crs/CS2103T tg/T01` followed by `yes`
* Deletes the student with student ID A1234567X, course CS2103T, and tutorial group T01.

`delete 3` followed by `no`
* No change is made.


### Finding students by name: `find`

Finds students whose names contain words that start with any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]...`

* The search is case-insensitive. e.g. `hans` matches `Hans`
* The order of keywords does not matter. e.g. `Hans Bo` matches `Bo Hans`
* Only the name field is searched
* Keywords match the **start of words** in names (prefix matching).Substrings in the middle of words are not matched.
    * e.g. `Han` matches `Hans`
    * `an` will not match `Hans`
* Persons matching at least one keyword are returned (i.e. `OR` search)
    * e.g. `Hans Bo` returns `Hans Gruber`, `Bo Yang`
* Keywords must contain only alphabetic characters (A–Z, a–z)

Examples:
* `find Jo` returns `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>

`delete 3` followed by `no`
* No change is made.

<a name="progress"></a>
### Updating a student's progress : `progress`

Updates a student's progress to either:
1. `on_track`
2. `needs_attention`
3. `at_risk`

<div markdown="span" class="alert alert-primary">
:bulb: **Tip:**<br><br>

To remove the progress tag of a student, use `not_set`
</div>

<a name="progressbyindex"></a>
**Update Progress by index**

Format: 
```
updateprogress INDEX p/PROGRESS
```

* Updates the progress of student at the specified `INDEX` to `PROGRESS`.
* The index refers to the index number shown in the currently displayed student list.
* The index **must be a positive integer** 1, 2, 3, …

<a name="progressbydetails"></a>
**Update progress by student details**

Format: 
```
updateprogress id/STUDENT_ID crs/COURSE_ID tg/TUTORIAL_GROUP p/PROGRESS
```

* Updates the progress of student with the exact details match for `STUDENT_ID`, `COURSE_ID`, and `TUTORIAL_GROUP` to `PROGRESS`.

**Examples**:
`progress 1 p/on_track`
* Sets the progress of the 1st student in the currently displayed student list to on_track.

`progress id/A1234567X crs/CS2103T tg/T01 p/needs_attention`
* Sets the progress of the student with student ID A1234567X, course CS2103T, and tutorial group T01 to needs_attention.

`progress 2 p/not_set`
* Clears the progress status of the 2nd student in the currently displayed student list.

<a name="clear"></a>

### Marking a student's attendance : `markattendance`
Updates a student's Attendance Status to either:
1. `y` --> Present  --> Green
2. `a` --> Absent   --> Red
3. `n` --> Undetermined   --> Grey

<div markdown="span" class="alert alert-primary"></div>
:bulb: **Tip:**<br><br>


<a name="attendancebyindex"></a>
**Update attendance by index, week, status**

Format:
```
markattendance INDEX week/WEEK sta/STATUS
```

* Updates the attendance of student at the specified `INDEX` and `WEEK` to `STATUS`.
* The index refers to the index number shown in the currently displayed student list.
* The index **must be a positive integer** 1, 2, 3, …
* The week referes to school weeks, which are visible to the right of teachassist

**Examples**:  
`markattendance 1 week/3 sta/y`
* marks the attendance of the 1st student's attendance in week 3 as present -> Green.

`markattendance 2 week/6 sta/a`
* marks the attendance of the 2nd student's attendance in week 6 as absent -> Red.

`markattendance 4 week/4 sta/n`
* marks the attendance of the 4th student's attendance in week 4 as unmarked -> Grey.
<a name="clear"></a>

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: 
```
clear
```

<a name='remark'></a>
* Adds a remark to the student at a particular index

Format:
```
remark INDEX txt/REMARK
```

**Examples**:
`remark 1 txt/Needs help with recursion`
* Adds remark "Needs help with recursion" to 1st student in the currently displayed student list.

<a name='unremark'></a>
* Removes the remark of a student at a particular index

Format:
```
unremark INDEX r/REMARK_INDEX
```
**Examples**:
`unremark 1 r/2`
* Deletes 2nd remark associated to the 1st student in the currently displayed student list.

<a name="exit"></a>
### Exiting the program : `exit`

Exits the program.

Format: 
```
exit
```

### Saving the data

TeachAssist data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME id/ID e/EMAIL crs/COURSE tg/TGROUP [tel/TELE]​` <br> e.g., `add n/James Ho p/A1234567X e/jamesho@u.nus.edu crs/CS2103t tg/T01 tel/im_a_ho`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`<br> or alternatively,  `delete id/STUDENT_ID crs/COURSE_ID tg/TUTORIAL_GROUP`<br> e.g., `delete id/A1234567X crs/CS2103T tg/T01`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list`
**Help**   | `help`
**Remark** | `remark INDEX txt/REMARK`
**Unremark** | `unremark INDEX r/REMARK_INDEX`
**Update Progress** | `updateprogress INDEX p/PROGRESS`<br> e.g., `progress 1 p/on_track`<br> or alternatively, `updateprogress id/STUDENT_ID crs/COURSE_ID tg/TUTORIAL_GROUP p/PROGRESS`<br> e.g., `progress id/A1234567X crs/CS2103T tg/T01 p/needs_attention`


--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q: Do I need to enter parameters in a fixed order?**
No. For commands with prefixes such as add and filter, parameters can be entered in any order as long as all required fields are provided.

**Q: Why did delete 1 remove a different student than I expected?**
Because the index refers to the current displayed list. You may be referring to an outdated list.
