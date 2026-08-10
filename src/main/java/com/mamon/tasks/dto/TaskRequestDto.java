package com.mamon.tasks.dto;

import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskRequestDto {
    @NotBlank(message = "Task Title can`t be blank")
    @Size(min = 3,max = 100 , message = "Task title must be between 3 and 100")
    private String title;

    @Size(max = 500 , message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Task status is required ")
    private TaskStatus status;

    @NotNull(message = "Task priority is required ")
    private TaskPriority priority;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}