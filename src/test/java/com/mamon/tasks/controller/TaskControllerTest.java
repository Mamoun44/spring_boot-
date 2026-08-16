package com.mamon.tasks.controller;

import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.exception.TaskNotFoundException;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldCreateTaskWith201Status() throws Exception {
        TaskResponseDto response = taskResponse(1L);
        when(taskService.createTask(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validTaskRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Write controller tests"));
    }

    @Test
    void shouldReturn400ForInvalidTaskRequest() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnTaskWith200Status() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(taskResponse(1L));

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void shouldReturn404WhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldDeleteTaskWith204Status() throws Exception {
        when(taskService.deleteTaskById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTaskById(1L);
    }

    private TaskResponseDto taskResponse(Long id) {
        return new TaskResponseDto(
                id,
                "Write controller tests",
                "Cover HTTP responses with MockMvc",
                TaskStatus.TODO,
                TaskPriority.HIGH,
                LocalDate.of(2030, 1, 1)
        );
    }

    private String validTaskRequest() {
        return """
                {
                  "title": "Write controller tests",
                  "description": "Cover HTTP responses with MockMvc",
                  "status": "TODO",
                  "priority": "HIGH",
                  "dueDate": "2030-01-01"
                }
                """;
    }
}
