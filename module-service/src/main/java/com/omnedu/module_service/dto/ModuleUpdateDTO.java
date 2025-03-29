package com.omnedu.module_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class ModuleUpdateDTO {
    private String name;
    private String description;
    private List<Long> taskIds;
}