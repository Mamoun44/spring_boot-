package com.mamon.tasks.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponseDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private List<TaskResponseDto> tasks;

    public ProjectResponseDto() {}

    public ProjectResponseDto(Long id, String name, String description, LocalDateTime createdDate, List<TaskResponseDto> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.tasks = tasks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public List<TaskResponseDto> getTasks() { return tasks; }
    public void setTasks(List<TaskResponseDto> tasks) { this.tasks = tasks; }
}