package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    Page<TaskResponseDto> getAllTasks(Pageable pageable);
    TaskResponseDto getTaskById(Long id);
    TaskResponseDto createTask(TaskRequestDto dto);
    TaskResponseDto updateTask(Long id, TaskRequestDto dto);
    TaskResponseDto updateTaskStatus(Long id, TaskStatus status);
    void deleteTaskById(Long id);

    List<TaskResponseDto> getTasksByStatus(TaskStatus status);
    List<TaskResponseDto> getTasksByPriority(TaskPriority priority);
    List<TaskResponseDto> getOverdueTasks();
}