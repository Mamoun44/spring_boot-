package com.mamon.tasks.repository;

import com.mamon.tasks.model.Project;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.specification.TaskSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldFindTasksByStatus() {
        saveTask("Todo task", TaskStatus.TODO, TaskPriority.LOW, LocalDate.now().plusDays(1), null);
        saveTask("Completed task", TaskStatus.COMPLETED, TaskPriority.LOW, LocalDate.now().plusDays(1), null);

        assertEquals(List.of("Todo task"), taskRepository.findAll(TaskSpecification.hasStatus(TaskStatus.TODO))
                .stream().map(Task::getTitle).toList());
    }

    @Test
    void shouldFindTasksByPriority() {
        saveTask("High priority", TaskStatus.TODO, TaskPriority.HIGH, LocalDate.now().plusDays(1), null);
        saveTask("Low priority", TaskStatus.TODO, TaskPriority.LOW, LocalDate.now().plusDays(1), null);

        assertEquals(List.of("High priority"), taskRepository.findAll(TaskSpecification.hasPriority(TaskPriority.HIGH))
                .stream().map(Task::getTitle).toList());
    }

    @Test
    void shouldFindTasksByProject() {
        Project firstProject = projectRepository.save(new Project(null, "First project", ""));
        Project secondProject = projectRepository.save(new Project(null, "Second project", ""));
        saveTask("First project task", TaskStatus.TODO, TaskPriority.LOW, LocalDate.now().plusDays(1), firstProject);
        saveTask("Second project task", TaskStatus.TODO, TaskPriority.LOW, LocalDate.now().plusDays(1), secondProject);

        assertEquals(List.of("First project task"), taskRepository.findAll(TaskSpecification.hasProject(firstProject.getId()))
                .stream().map(Task::getTitle).toList());
    }

    @Test
    void shouldFindOnlyIncompletePastDueTasks() {
        saveTask("Overdue", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, LocalDate.now().minusDays(1), null);
        saveTask("Completed past due", TaskStatus.COMPLETED, TaskPriority.HIGH, LocalDate.now().minusDays(1), null);
        saveTask("Upcoming", TaskStatus.TODO, TaskPriority.HIGH, LocalDate.now().plusDays(1), null);

        assertEquals(List.of("Overdue"), taskRepository.findAll(TaskSpecification.isOverdue(true))
                .stream().map(Task::getTitle).toList());
    }

    private void saveTask(String title, TaskStatus status, TaskPriority priority,
                          LocalDate dueDate, Project project) {
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setProject(project);
        taskRepository.save(task);
    }
}
