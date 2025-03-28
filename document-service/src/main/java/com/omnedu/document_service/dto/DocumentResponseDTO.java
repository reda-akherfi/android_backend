package com.omnedu.document_service.dto;

import com.omnedu.document_service.model.DocumentEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Data
public class DocumentResponseDTO {
    private String id;
    private String name;
    private String contentType;
    private long size;
    private LocalDateTime uploadDate;
    private String downloadUrl;
    private String userId;
    private List<Long> taskIds;
    private String description;

    public DocumentResponseDTO(DocumentEntity entity, String baseUrl) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.contentType = entity.getContentType();
        this.size = entity.getSize();
        this.uploadDate = entity.getUploadDate();
        this.downloadUrl = baseUrl + "/api/documents/" + entity.getId();
        this.userId = entity.getUserId();
        this.description = entity.getDescription();
        
        if (entity.getTaskIds() != null && !entity.getTaskIds().isEmpty()) {
            this.taskIds = Arrays.stream(entity.getTaskIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }
}