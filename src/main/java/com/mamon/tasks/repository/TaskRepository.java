package com.mamon.tasks.repository;

import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskPriority;
import com.mamon.tasks.model.TaskStatus;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface  TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByProjectId(Long projectId);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);

}
