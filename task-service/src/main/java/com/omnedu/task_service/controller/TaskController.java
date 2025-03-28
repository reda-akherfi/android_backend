package com.omnedu.task_service.controller;

import com.omnedu.task_service.dto.TaskRequestDTO;
import com.omnedu.task_service.dto.TaskResponseDTO;
import com.omnedu.task_service.model.Task;
import com.omnedu.task_service.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final HttpServletRequest request;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(@RequestBody @Valid TaskRequestDTO requestDTO) {
        String userId = getUserIdFromRequest();
        return taskService.createTask(requestDTO, userId);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTask(@PathVariable Long id) {
        String userId = getUserIdFromRequest();
        return taskService.getTaskById(id, userId);
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        String userId = getUserIdFromRequest();
        return taskService.getAllTasksByUser(userId);
    }

    @GetMapping("/status/{status}")
    public List<TaskResponseDTO> getTasksByStatus(@PathVariable Task.Status status) {
        String userId = getUserIdFromRequest();
        return taskService.getTasksByStatus(userId, status);
    }

    @GetMapping("/priority/{priority}")
    public List<TaskResponseDTO> getTasksByPriority(@PathVariable Task.Priority priority) {
        String userId = getUserIdFromRequest();
        return taskService.getTasksByPriority(userId, priority);
    }

    @GetMapping("/overdue")
    public List<TaskResponseDTO> getOverdueTasks() {
        String userId = getUserIdFromRequest();
        return taskService.getOverdueTasks(userId);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequestDTO requestDTO) {
        String userId = getUserIdFromRequest();
        return taskService.updateTask(id, requestDTO, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        String userId = getUserIdFromRequest();
        taskService.deleteTask(id, userId);
    }

    @GetMapping("/batch")
    public List<TaskResponseDTO> getTasksByIds(
            @RequestParam List<Long> ids,
            @RequestHeader("X-User-Id") String userId) {
        return taskService.getTasksByIds(ids, userId);
    }

    private String getUserIdFromRequest() {
        return request.getHeader("X-User-Id");
    }
}