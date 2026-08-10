package com.mamon.tasks.mapper;

import com.mamon.tasks.dto.ProjectRequestDto;
import com.mamon.tasks.dto.ProjectResponseDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.model.Project;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectResponseDto toResponseDto(Project project) {
        if (project == null) return null;
        List<TaskResponseDto> taskDtos = project.getTasks() != null ?
                project.getTasks().stream().map(TaskMapper::toResponseDto).collect(Collectors.toList()) :
                Collections.emptyList();

        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedDate(),
                taskDtos
        );
    }

    public static Project toEntity(ProjectRequestDto dto) {
        if (dto == null) return null;
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        return project;
    }
}