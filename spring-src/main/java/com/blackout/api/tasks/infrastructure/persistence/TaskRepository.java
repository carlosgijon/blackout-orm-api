package com.blackout.api.tasks.infrastructure.persistence;

import com.blackout.api.tasks.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByBandIdOrderByCreatedAtDesc(String bandId);
}
