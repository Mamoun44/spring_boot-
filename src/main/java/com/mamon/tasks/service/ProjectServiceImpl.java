package com.mamon.tasks.service;

import com.mamon.tasks.dto.ProjectResponseDto;
import com.mamon.tasks.exception.ProjectNotFoundException;
import com.mamon.tasks.mapper.ProjectMapper;
import com.mamon.tasks.model.Project;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.repository.ProjectRepository;
import com.mamon.tasks.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toResponseDto)
                .collect(Collectors.toList()).reversed();
    }

    @Override
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.toResponseDto(project);
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Task addTaskToProject(Long projectId, Task task) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            task.setProject(projectOptional.get());
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public boolean updateProject(Project updatedProject, Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            projectRepository.save(project);
            return true;
        }

        return false;
    }


}