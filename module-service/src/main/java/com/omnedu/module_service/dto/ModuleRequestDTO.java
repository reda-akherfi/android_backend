package com.omnedu.module_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModuleRequestDTO {
    private String name;
    private String description;
    private List<Long> taskIds;

    // Getters and Setters
}