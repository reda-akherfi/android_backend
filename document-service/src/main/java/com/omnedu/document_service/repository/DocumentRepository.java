package com.omnedu.document_service.repository;

import com.omnedu.document_service.model.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {
    
    // Find all documents for a specific user
    List<DocumentEntity> findByUserId(String userId);
    
    // Find documents for a specific user that are associated with a particular task
    List<DocumentEntity> findByUserIdAndTaskIdsContaining(String userId, String taskId);
    
    // Optional: Find documents by task ID (without user filter)
    List<DocumentEntity> findByTaskIdsContaining(String taskId);
}