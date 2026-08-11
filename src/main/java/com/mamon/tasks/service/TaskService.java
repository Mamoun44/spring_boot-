package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> getAllTasks();
    TaskResponseDto getTaskById(Long id);
    TaskResponseDto createTask(TaskRequestDto dto);
    boolean  updateTaskById(Long id, TaskRequestDto dto);
    boolean  updateTaskStatus(Long id, TaskStatus status);
    boolean  deleteTaskById(Long id);

    List<TaskResponseDto> getTasksByStatus(TaskStatus status);
    List<TaskResponseDto> getTasksByPriority(TaskPriority priority);
    List<TaskResponseDto> getOverdueTasks();
}