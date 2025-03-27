package com.omnedu.task_service.service;

import com.omnedu.task_service.dto.TaskRequestDTO;
import com.omnedu.task_service.dto.TaskResponseDTO;
import com.omnedu.task_service.exception.TaskNotFoundException;
import com.omnedu.task_service.model.Task;
import com.omnedu.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponseDTO createTask(TaskRequestDTO request, String userId) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.Status.PENDING);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Task.Priority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setUserId(userId);

        Task savedTask = taskRepository.save(task);
        return new TaskResponseDTO(savedTask);
    }

    public TaskResponseDTO getTaskById(Long id, String userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (!task.getUserId().equals(userId)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }

        return new TaskResponseDTO(task);
    }

    public List<TaskResponseDTO> getAllTasksByUser(String userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
public TaskResponseDTO updateTask(Long id, TaskRequestDTO request, String userId) {
    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    
    if (!task.getUserId().equals(userId)) {
        throw new TaskNotFoundException("Task not found with id: " + id);
    }

    // Only update fields that are provided in the request
    if (request.getTitle() != null && !request.getTitle().isBlank()) {
        task.setTitle(request.getTitle());
    }
    if (request.getDescription() != null) {
        task.setDescription(request.getDescription());
    }
    if (request.getStatus() != null) {
        task.setStatus(request.getStatus());
    }
    if (request.getPriority() != null) {
        task.setPriority(request.getPriority());
    }
    if (request.getDueDate() != null) {
        task.setDueDate(request.getDueDate());
    }

    Task updatedTask = taskRepository.save(task);
    return new TaskResponseDTO(updatedTask);
}

    @Transactional
    public void deleteTask(Long id, String userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (!task.getUserId().equals(userId)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }

        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> getTasksByStatus(String userId, Task.Status status) {
        return taskRepository.findByUserIdAndStatus(userId, status).stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByPriority(String userId, Task.Priority priority) {
        return taskRepository.findByUserIdAndPriority(userId, priority).stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getOverdueTasks(String userId) {
        return taskRepository.findByUserIdAndDueDateBefore(userId, LocalDateTime.now()).stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
    }
}