package service;

import model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining CRUD operations for Task storage.
 * Follows the Repository Pattern for separation of concerns.
 */
public interface TaskRepository {
    void         save(Task task);
    Optional<Task> findById(int id);
    List<Task>   findAll();
    List<Task>   findByStatus(Task.Status status);
    List<Task>   findByPriority(Task.Priority priority);
    boolean      deleteById(int id);
    boolean      exists(int id);
    int          count();
}
