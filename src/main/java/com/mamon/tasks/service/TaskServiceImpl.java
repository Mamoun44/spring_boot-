package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.InvalidTaskStatusException;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.mapper.TaskMapper;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.TaskRepository;
import com.mamon.tasks.specification.TaskSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Page<TaskResponseDto> getTasks(TaskStatus status, TaskPriority priority,
                                          Long projectId, Boolean overdue, Pageable pageable) {
        Specification<Task> specification = Specification.where(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.hasProject(projectId))
                .and(TaskSpecification.isOverdue(overdue));

        return taskRepository.findAll(specification, pageable).map(TaskMapper::toResponseDto);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task savedTask = taskRepository.save(TaskMapper.toEntity(dto));
        logger.info("Created task with id={}", savedTask.getId());
        return TaskMapper.toResponseDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        return TaskMapper.toResponseDto(findTaskById(id));
    }

    @Override
    public boolean updateTaskById(Long id, TaskRequestDto dto) {
        Task task = findTaskById(id);
        validateStatusTransition(task, dto.getStatus());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        taskRepository.save(task);
        logger.info("Updated task with id={}", id);
        return true;
    }

    @Override
    public boolean updateTaskStatus(Long id, TaskStatus status) {
        Task task = findTaskById(id);
        validateStatusTransition(task, status);
        task.setStatus(status);
        taskRepository.save(task);
        logger.info("Updated status for task id={} to {}", id, status);
        return true;
    }

    private void validateStatusTransition(Task task, TaskStatus newStatus) {
        if (task.getStatus() == TaskStatus.COMPLETED && newStatus != TaskStatus.COMPLETED) {
            logger.warn("Rejected invalid status transition for task id={}: {} to {}",
                    task.getId(), task.getStatus(), newStatus);
            throw new InvalidTaskStatusException(
                    "A completed task cannot transition to " + newStatus);
        }
    }

    @Override
    public boolean deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            logger.warn("Task with id={} was not found for deletion", id);
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
        logger.info("Deleted task with id={}", id);
        return true;
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Task with id={} was not found", id);
                    return new TaskNotFoundException(id);
                });
    }
}
