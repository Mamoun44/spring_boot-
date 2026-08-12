package com.mamon.tasks.service;

import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.InvalidTaskStatusException;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import com.mamon.tasks.dto.TaskRequestDto;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldReturnTaskWhenTaskExists() {

        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Testing");
        task.setDescription("Learn JUnit and Mockito");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        TaskResponseDto result =
                taskService.getTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Learn Testing", result.getTitle());
        assertEquals(
                "Learn JUnit and Mockito",
                result.getDescription()
        );
    }
    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {

        // Arrange
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(99L)
        );
    }
    @Test
    void shouldCreateTaskSuccessfully() {

        // Arrange
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Learn Mockito");
        request.setDescription("Practice Mockito testing");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Learn Mockito");
        savedTask.setDescription("Practice Mockito testing");
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setPriority(TaskPriority.HIGH);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        // Act
        TaskResponseDto result =
                taskService.createTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Learn Mockito", result.getTitle());

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task taskPassedToRepository =
                taskCaptor.getValue();

        assertEquals(
                "Learn Mockito",
                taskPassedToRepository.getTitle()
        );

        assertEquals(
                "Practice Mockito testing",
                taskPassedToRepository.getDescription()
        );

        assertEquals(
                TaskStatus.TODO,
                taskPassedToRepository.getStatus()
        );

        assertEquals(
                TaskPriority.HIGH,
                taskPassedToRepository.getPriority()
        );
    }

    @Test
    void shouldDeleteTaskWhenTaskExists() {

        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean deleted = taskService.deleteTaskById(1L);

        // Assert
        assertTrue(deleted);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingTask() {

        // Arrange
        when(taskRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTaskById(99L)
        );
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldRejectTransitionFromCompletedToNonCompletedStatus() {

        // Arrange
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setStatus(TaskStatus.COMPLETED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(completedTask));

        // Act & Assert
        assertThrows(
                InvalidTaskStatusException.class,
                () -> taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS)
        );
        assertEquals(TaskStatus.COMPLETED, completedTask.getStatus());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldRejectInvalidStatusTransitionDuringFullUpdate() {

        // Arrange
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setStatus(TaskStatus.COMPLETED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(completedTask));

        TaskRequestDto request = new TaskRequestDto();
        request.setStatus(TaskStatus.TODO);

        // Act & Assert
        assertThrows(
                InvalidTaskStatusException.class,
                () -> taskService.updateTaskById(1L, request)
        );
        assertEquals(TaskStatus.COMPLETED, completedTask.getStatus());
        verify(taskRepository, never()).save(any(Task.class));
    }
}
