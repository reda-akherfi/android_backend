package com.omnedu.document_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentRequestDTO {
    private MultipartFile file;
    private String description; // Optional field for future use
}