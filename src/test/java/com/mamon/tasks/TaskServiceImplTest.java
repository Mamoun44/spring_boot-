package com.mamon.tasks;

import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.repository.TaskRepository;
import com.mamon.tasks.service.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
}