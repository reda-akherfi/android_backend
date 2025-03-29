// TaskDTO.java
package com.omnedu.noteservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
}