package com.mamon.tasks.service;

import com.mamon.tasks.model.Project;
import com.mamon.tasks.model.Task;
import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project createProject(Project project);
    boolean deleteProject(Long id);
    Task addTaskToProject(Long projectId, Task task);
    List<Task> getTasksByProjectId(Long projectId);

    boolean updateProject(Project updatedProject, Long id);
}