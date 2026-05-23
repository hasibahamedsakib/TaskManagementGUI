package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    public enum Priority {
        LOW, MEDIUM, HIGH;
        public String getLabel() {
            if (this == LOW) return "Low";
            if (this == MEDIUM) return "Medium";
            return "High";
        }
    }

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED;
        public String getLabel() {
            if (this == PENDING) return "Pending";
            if (this == IN_PROGRESS) return "In Progress";
            return "Completed";
        }
    }

    private static int idCounter = 1;

    private final int id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;
    private final LocalDate createdDate;

    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this.id          = idCounter++;
        this.title       = title;
        this.description = description;
        this.priority    = priority;
        this.status      = Status.PENDING;
        this.dueDate     = dueDate;
        this.createdDate = LocalDate.now();
    }

    public int       getId()          { return id; }
    public String    getTitle()       { return title; }
    public String    getDescription() { return description; }
    public Priority  getPriority()    { return priority; }
    public Status    getStatus()      { return status; }
    public LocalDate getDueDate()     { return dueDate; }
    public LocalDate getCreatedDate() { return createdDate; }

    public void setTitle(String title)             { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority)     { this.priority = priority; }
    public void setStatus(Status status)           { this.status = status; }
    public void setDueDate(LocalDate dueDate)      { this.dueDate = dueDate; }

    public boolean isCompleted() { return status == Status.COMPLETED; }
    public boolean isOverdue()   { return !isCompleted() && dueDate != null && dueDate.isBefore(LocalDate.now()); }

    public String getFormattedDueDate() {
        return dueDate != null ? dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "N/A";
    }
    public String getFormattedCreatedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
