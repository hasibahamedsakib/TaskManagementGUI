package service;

import model.Task;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(String title, String description, Task.Priority priority, LocalDate dueDate) {
        validateTitle(title);
        Task task = new Task(title.trim(), description.trim(), priority, dueDate);
        repository.save(task);
        return task;
    }

    public List<Task> getAllTasks()                         { return repository.findAll(); }
    public Optional<Task> getTaskById(int id)              { return repository.findById(id); }
    public List<Task> getTasksByStatus(Task.Status s)      { return repository.findByStatus(s); }
    public List<Task> getTasksByPriority(Task.Priority p)  { return repository.findByPriority(p); }

    public List<Task> getOverdueTasks() {
        List<Task> result = new ArrayList<Task>();
        for (Task t : repository.findAll()) if (t.isOverdue()) result.add(t);
        return result;
    }

    public boolean updateTask(int id, String newTitle, String newDescription,
                              Task.Priority newPriority, LocalDate newDueDate) {
        Optional<Task> opt = repository.findById(id);
        if (!opt.isPresent()) return false;
        Task task = opt.get();
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            validateTitle(newTitle);
            task.setTitle(newTitle.trim());
        }
        if (newDescription != null) task.setDescription(newDescription.trim());
        if (newPriority    != null) task.setPriority(newPriority);
        if (newDueDate     != null) task.setDueDate(newDueDate);
        repository.save(task);
        return true;
    }

    public boolean markAsInProgress(int id) { return changeStatus(id, Task.Status.IN_PROGRESS); }
    public boolean markAsCompleted(int id)  { return changeStatus(id, Task.Status.COMPLETED); }
    public boolean markAsPending(int id)    { return changeStatus(id, Task.Status.PENDING); }
    public boolean deleteTask(int id)       { return repository.deleteById(id); }

    public int getTotalCount()      { return repository.count(); }
    public int getCompletedCount()  { return repository.findByStatus(Task.Status.COMPLETED).size(); }
    public int getPendingCount()    { return repository.findByStatus(Task.Status.PENDING).size(); }
    public int getInProgressCount() { return repository.findByStatus(Task.Status.IN_PROGRESS).size(); }
    public int getOverdueCount()    { return getOverdueTasks().size(); }

    private boolean changeStatus(int id, Task.Status newStatus) {
        Optional<Task> opt = repository.findById(id);
        if (!opt.isPresent()) return false;
        opt.get().setStatus(newStatus);
        repository.save(opt.get());
        return true;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Task title cannot be empty.");
        if (title.trim().length() > 100)
            throw new IllegalArgumentException("Task title must be 100 characters or less.");
    }
}
