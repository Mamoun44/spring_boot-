package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.mapper.TaskMapper;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.TaskRepository;
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
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponseDto)
                .toList();
    }
    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = TaskMapper.toEntity(dto);
        Task saved = taskRepository.save(task);
        return TaskMapper.toResponseDto(saved);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return TaskMapper.toResponseDto(task);
    }


    @Override
    public boolean updateTaskById(Long id, TaskRequestDto dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        taskRepository.save(task);

        return true;
    }

    @Override
    public boolean updateTaskStatus(Long id, TaskStatus status) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(status);

        taskRepository.save(task);

        return true;
    }

    @Override
    public boolean deleteTaskById(Long id) {

        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);

        return true;
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {

        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(TaskMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now())
                .stream()
                .map(TaskMapper::toResponseDto)
                .toList();
    }
}