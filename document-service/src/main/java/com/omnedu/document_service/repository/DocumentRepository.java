package com.omnedu.document_service.repository;

import com.omnedu.document_service.model.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {
}