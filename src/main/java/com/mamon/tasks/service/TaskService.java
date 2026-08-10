package com.mamon.tasks.service;

import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskStatus;

import java.util.List;

public interface TaskService {
    List<Task> getTasks();
    void creatTask(Task task);

    Task getTaskById(Long id);

    boolean deleteTaskById(Long id);

    boolean updateTaskById(Long id, Task updatedTask);
    boolean updateTaskStatus(Long id, TaskStatus status);
    List<Task> getTasksByStatus(TaskStatus status);
}
