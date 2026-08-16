package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.exception.ProjectNotFoundException;
import com.mamon.tasks.model.Project;
import com.mamon.tasks.repository.ProjectRepository;
import com.mamon.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void shouldThrowExceptionWhenProjectDoesNotExist() {

        // Arrange
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Testing");

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.addTaskToProject(
                        999L,
                        request
                )
        );

        // Make sure no task was created
        verify(taskRepository, never())
                .save(any());
    }
}