package com.omnedu.module_service.dto;

import lombok.Data;

import java.util.List;
import com.omnedu.module_service.model.Module;

@Data
public class ModuleResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> taskIds;

    // Constructor
    public ModuleResponseDTO(Module module) {
        this.id = module.getId();
        this.name = module.getName();
        this.description = module.getDescription();
        this.taskIds = module.getTaskIds();
    }
}