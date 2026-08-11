package com.mamon.tasks.service;

import com.mamon.tasks.dto.ProjectRequestDto;
import com.mamon.tasks.dto.ProjectResponseDto;
import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.DuplicateProjectNameException;
import com.mamon.tasks.exception.ProjectNotFoundException;
import com.mamon.tasks.mapper.ProjectMapper;
import com.mamon.tasks.mapper.TaskMapper;
import com.mamon.tasks.model.Project;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.ProjectRepository;
import com.mamon.tasks.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.toResponseDto(project);
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        if (projectRepository.existsByName(dto.getName())) {
            throw new DuplicateProjectNameException(dto.getName());
        }
        Project project = ProjectMapper.toEntity(dto);
        Project saved = projectRepository.save(project);
        return ProjectMapper.toResponseDto(saved);
    }

    @Override
    public boolean updateProject(Long id, ProjectRequestDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        if (!project.getName().equalsIgnoreCase(dto.getName()) && projectRepository.existsByName(dto.getName())) {
            throw new DuplicateProjectNameException(dto.getName());
        }

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        projectRepository.save(project);
        return true;
    }

    @Override
    public boolean deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
        return true;
    }

    @Override
    public TaskResponseDto addTaskToProject(Long projectId, TaskRequestDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Task task = TaskMapper.toEntity(dto);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        task.setProject(project);
        Task saved = taskRepository.save(task);
        return TaskMapper.toResponseDto(saved);
    }

    @Override
    public List<TaskResponseDto> getTasksByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        return taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toResponseDto)
                .toList();
    }
}