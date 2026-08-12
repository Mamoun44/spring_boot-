package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.mapper.TaskMapper;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.TaskRepository;
import com.mamon.tasks.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Page<TaskResponseDto> getTasks(
            TaskStatus status,
            TaskPriority priority,
            Long projectId,
            Boolean overdue,
            Pageable pageable) {

        Specification<Task> specification =
                Specification.where(
                        TaskSpecification.hasStatus(status)
                ).and(
                        TaskSpecification.hasPriority(priority)
                ).and(
                        TaskSpecification.hasProject(projectId)
                ).and(
                        TaskSpecification.isOverdue(overdue)
                );

        return taskRepository
                .findAll(specification, pageable)
                .map(TaskMapper::toResponseDto);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {

        Task task = TaskMapper.toEntity(dto);

        Task savedTask = taskRepository.save(task);

        return TaskMapper.toResponseDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return TaskMapper.toResponseDto(task);
    }

    @Override
    public boolean updateTaskById(
            Long id,
            TaskRequestDto dto) {

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
    public boolean updateTaskStatus(
            Long id,
            TaskStatus status) {

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
}