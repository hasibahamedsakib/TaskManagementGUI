# 📋 Task Management System — GUI Edition

> A modern, dark-themed Java Swing desktop application for managing tasks efficiently.
> Built as a university group project demonstrating core **Object-Oriented Programming** principles.

---

## 🖥️ Preview

```
┌─────────────────────────────────────────────────────────────────┐
│  TaskBoard                          🔍 Search tasks...  [+ Add] │
├──────────────┬──────────────────────────────────────────────────┤
│  >> VIEWS    │  All Tasks   5 tasks                             │
│              │  ┌──────┬──────────┬────────────┬──────────────┐ │
│  All Tasks   │  │  5   │    3     │     1      │      1       │ │
│  Pending     │  │Total │ Pending  │ In Progress│  Completed   │ │
│  In Progress │  └──────┴──────────┴────────────┴──────────────┘ │
│  Completed   │                                                   │
│  Overdue ⚠   │  ▌ Design database schema                       │
│              │    IN PROGRESS  •  High Priority  📅 26-05-2026  │
│              │                              [→ Status] [Edit] [✕]│
└──────────────┴──────────────────────────────────────────────────┘
```

---

## ✨ Features

| Feature | Description |
|---|---|
| ➕ **Add Task** | Create tasks with title, description, priority and due date |
| ✏️ **Edit Task** | Update any field via a clean modal dialog |
| 🗑️ **Delete Task** | Remove tasks with a confirmation prompt |
| 🔄 **Status Cycling** | One click: Pending → In Progress → Completed → Pending |
| 🔍 **Live Search** | Filter tasks instantly as you type |
| 📊 **Dashboard Stats** | Real-time counters for all task states |
| 🗂️ **Sidebar Filters** | Navigate by All / Pending / In Progress / Completed / Overdue |
| ⚠️ **Overdue Detection** | Automatically flags tasks past their due date |
| 🎨 **Dark Theme** | Fully custom-painted dark UI — no system L&F dependencies |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17 or later** — check your version:
  ```
  java -version
  ```
  If needed, download from: https://www.oracle.com/java/technologies/downloads/

### Run (easiest — pre-compiled JAR)

```bash
java -jar TaskManagementGUI.jar
```

### Compile from source

```bash
# 1. Compile all source files
javac --release 17 -d out -sourcepath src src/Main.java

# 2. Run
java -cp out Main

# 3. (Optional) Package into a JAR
jar cfe TaskManagementGUI.jar Main -C out .
```

---

## 🗂️ Project Structure

```
TaskManagementGUI/
├── src/
│   ├── Main.java                          ← Entry point
│   ├── model/
│   │   └── Task.java                      ← Task entity + Priority/Status enums
│   ├── service/
│   │   ├── TaskRepository.java            ← Repository interface (Abstraction)
│   │   ├── InMemoryTaskRepository.java    ← ArrayList-backed implementation
│   │   └── TaskService.java               ← Business logic & validation
│   └── ui/
│       ├── Theme.java                     ← Design tokens (colours, fonts, sizes)
│       ├── MainWindow.java                ← Main application window
│       └── components/
│           ├── Widgets.java               ← Reusable UI building blocks
│           ├── TaskCard.java              ← Animated task row component
│           ├── TaskDialog.java            ← Add / Edit modal dialog
│           └── StatCard.java              ← Dashboard statistic card
├── TaskManagementGUI.jar                  ← Executable JAR
└── README.md
```

---

## 🧱 OOP Concepts Applied

### Encapsulation
All `Task` fields are `private`. External code can only read or modify data through defined getters and setters, preventing uncontrolled state changes.

```java
// Task.java
private String title;
private Status status;

public String getTitle()          { return title; }
public void   setStatus(Status s) { this.status = s; }
```

### Abstraction
`TaskRepository` is an **interface** that defines *what* operations are available, without exposing *how* they work. The UI only ever talks to the interface — the storage implementation is hidden.

```java
// TaskRepository.java
public interface TaskRepository {
    void           save(Task task);
    Optional<Task> findById(int id);
    List<Task>     findAll();
    boolean        deleteById(int id);
}
```

### Polymorphism
`InMemoryTaskRepository` implements `TaskRepository`. The service layer works against the interface type, making it trivial to swap in a file-based or database-backed repository later.

```java
// TaskService.java — depends on the interface, not the concrete class
public TaskService(TaskRepository repository) {
    this.repository = repository;
}
```

### Inheritance / Enums with Behaviour
`Task.Priority` and `Task.Status` are enums that carry their own display logic through an overridden `getLabel()` method — a form of type-safe inheritance.

```java
// Task.java
public enum Priority {
    LOW, MEDIUM, HIGH;
    public String getLabel() {
        if (this == HIGH)   return "High";
        if (this == MEDIUM) return "Medium";
        return "Low";
    }
}
```

---

## 🏗️ Architecture

The project follows a **3-tier layered architecture** to keep concerns cleanly separated:

```
┌─────────────────────────────┐
│         UI Layer            │  MainWindow, TaskCard, TaskDialog
│   (Swing / Presentation)    │  No business logic here
├─────────────────────────────┤
│       Service Layer         │  TaskService
│   (Business Logic)          │  Validation, CRUD orchestration
├─────────────────────────────┤
│      Repository Layer       │  TaskRepository (interface)
│   (Data Access)             │  InMemoryTaskRepository (ArrayList)
├─────────────────────────────┤
│        Model Layer          │  Task, Task.Priority, Task.Status
│   (Domain Objects)          │  Pure data + enums
└─────────────────────────────┘
```

Each layer only communicates with the layer directly below it. The UI never touches the repository directly.

---

## 🛠️ Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17+ |
| GUI Framework | Java Swing (custom-painted) |
| Data Storage | `ArrayList<Task>` (in-memory) |
| Build | `javac` / executable JAR |
| Icons | Custom-rendered via Java2D |

> No external libraries required — runs anywhere Java 17+ is installed.

---

## 👥 Group Members

| Name | Student ID | Role |
|---|---|---|
| Hasib Ahmed Sakib | 42240201831 | Project Lead & Backend Architecture |
| Fatema Akter Reya | 42240201819 | GUI Development (Swing) |
| Dipantor Chandra | 42240201840 | OOP Design & Documentation |
| Sumiya Akter | 42240201803 | Console UI & Input Handling |
| Ema | 42240201735 | Testing & Quality Assurance |
| Kawser Islam | 42240201736 | UI Components & Presentation |

---

## 🎓 Academic Information

| | |
|---|---|
| **University** | Northern University Bangladesh |
| **Course** | Object-Oriented Programming (OOP) |
| **Instructor** | Simon Bin Akter |
| **Semester** | 2026 |

---

## 📄 License

This project was developed for academic purposes at Northern University Bangladesh.
