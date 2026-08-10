package com.mamon.tasks.service;

import com.mamon.tasks.dto.ProjectRequestDto;
import com.mamon.tasks.dto.ProjectResponseDto;
import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;

import java.util.List;

public interface ProjectService {
    List<ProjectResponseDto> getAllProjects();
    ProjectResponseDto getProjectById(Long id);
    ProjectResponseDto createProject(ProjectRequestDto dto);
    ProjectResponseDto updateProject(Long id, ProjectRequestDto dto);
    void deleteProject(Long id);

    TaskResponseDto addTaskToProject(Long projectId, TaskRequestDto dto);
    List<TaskResponseDto> getTasksByProjectId(Long projectId);
}