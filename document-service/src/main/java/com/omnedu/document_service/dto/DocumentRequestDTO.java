package com.omnedu.document_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DocumentRequestDTO {
    private MultipartFile file;
    private String description;
    private List<Long> taskIds; // Added for task association
}