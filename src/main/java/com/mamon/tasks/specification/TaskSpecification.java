package com.mamon.tasks.specification;

import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> hasStatus(TaskStatus status) {

        return (root, query, criteriaBuilder) ->
                status == null
                        ? null
                        : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {

        return (root, query, criteriaBuilder) ->
                priority == null
                        ? null
                        : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasProject(Long projectId) {

        return (root, query, criteriaBuilder) ->
                projectId == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("project").get("id"),
                        projectId
                );
    }

    public static Specification<Task> isOverdue(Boolean overdue) {

        return (root, query, criteriaBuilder) -> {

            if (overdue == null) {
                return null;
            }

            if (overdue) {
                return criteriaBuilder.and(
                        criteriaBuilder.lessThan(
                                root.get("dueDate"),
                                LocalDate.now()
                        ),
                        criteriaBuilder.notEqual(
                                root.get("status"),
                                TaskStatus.COMPLETED
                        )
                );
            }

            return criteriaBuilder.or(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("dueDate"),
                            LocalDate.now()
                    ),
                    criteriaBuilder.equal(
                            root.get("status"),
                            TaskStatus.COMPLETED
                    )
            );
        };
    }
}