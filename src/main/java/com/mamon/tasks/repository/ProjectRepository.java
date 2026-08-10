package com.mamon.tasks.repository;

import com.mamon.tasks.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByName(String name);
    boolean existsByName(String name);
}
