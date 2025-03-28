package com.omnedu.task_service.repository;

import com.omnedu.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(String userId);
    List<Task> findByUserIdAndStatus(String userId, Task.Status status);
    List<Task> findByUserIdAndPriority(String userId, Task.Priority priority);
    List<Task> findByUserIdAndDueDateBefore(String userId, LocalDateTime dueDate);
    List<Task> findByIdInAndUserId(List<Long> ids, String userId);
}