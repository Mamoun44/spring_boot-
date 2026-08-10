package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.mapper.TaskMapper;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskMapper::toResponseDto);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toResponseDto(task);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = TaskMapper.toEntity(dto);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        Task saved = taskRepository.save(task);
        return TaskMapper.toResponseDto(saved);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        Task updated = taskRepository.save(task);
        return TaskMapper.toResponseDto(updated);
    }

    @Override
    public TaskResponseDto updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return TaskMapper.toResponseDto(updated);
    }

    @Override
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority).stream()
                .map(TaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now()).stream()
                .map(TaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}