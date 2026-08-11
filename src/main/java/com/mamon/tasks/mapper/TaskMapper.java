package com.mamon.tasks.mapper;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.model.Task;

public class TaskMapper {

    public static TaskResponseDto toResponseDto(Task task) {
        if (task == null) return null;

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate()
        );
    }

    public static Task toEntity(TaskRequestDto dto) {
        if (dto == null) return null;
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }
}