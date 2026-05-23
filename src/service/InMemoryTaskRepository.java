package service;

import model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {

    private final ArrayList<Task> tasks = new ArrayList<Task>();

    public void save(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                return;
            }
        }
        tasks.add(task);
    }

    public Optional<Task> findById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return Optional.of(t);
        }
        return Optional.empty();
    }

    public List<Task> findAll() {
        return new ArrayList<Task>(tasks);
    }

    public List<Task> findByStatus(Task.Status status) {
        List<Task> result = new ArrayList<Task>();
        for (Task t : tasks) if (t.getStatus() == status) result.add(t);
        return result;
    }

    public List<Task> findByPriority(Task.Priority priority) {
        List<Task> result = new ArrayList<Task>();
        for (Task t : tasks) if (t.getPriority() == priority) result.add(t);
        return result;
    }

    public boolean deleteById(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) { tasks.remove(i); return true; }
        }
        return false;
    }

    public boolean exists(int id) {
        for (Task t : tasks) if (t.getId() == id) return true;
        return false;
    }

    public int count() { return tasks.size(); }
}
