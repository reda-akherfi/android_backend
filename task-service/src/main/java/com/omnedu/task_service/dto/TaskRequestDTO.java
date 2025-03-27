package com.omnedu.task_service.dto;

import com.omnedu.task_service.model.Task;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDTO {

    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Task.Status status;

    private Task.Priority priority;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;
}