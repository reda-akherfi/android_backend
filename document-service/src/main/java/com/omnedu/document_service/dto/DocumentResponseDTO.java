package com.omnedu.document_service.dto;

import com.omnedu.document_service.model.DocumentEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {
    private String id;
    private String name;
    private String contentType;
    private long size;
    private LocalDateTime uploadDate;
    private String downloadUrl;

    public DocumentResponseDTO(DocumentEntity entity, String baseUrl) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.contentType = entity.getContentType();
        this.size = entity.getSize();
        this.uploadDate = entity.getUploadDate();
        this.downloadUrl = baseUrl + "/api/documents/" + entity.getId();
    }
}