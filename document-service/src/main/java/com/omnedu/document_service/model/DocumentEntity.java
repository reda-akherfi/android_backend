package com.omnedu.document_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "documents")
public class DocumentEntity {
    @Id
    private String id;
    private String name;
    private String contentType;
    private long size;
    private byte[] content;
    private LocalDateTime uploadDate;
    private String description;
}