package com.omnedu.document_service.service;

import com.omnedu.document_service.client.TaskServiceClient;
import com.omnedu.document_service.dto.DocumentResponseDTO;
import com.omnedu.document_service.dto.TaskDTO;
import com.omnedu.document_service.exception.DocumentNotFoundException;
import com.omnedu.document_service.model.DocumentEntity;
import com.omnedu.document_service.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TaskServiceClient taskServiceClient;

    @Transactional
    public DocumentResponseDTO store(MultipartFile file, String baseUrl, String userId, List<Long> taskIds) {
        try {
            // Validate tasks if provided
            if (taskIds != null && !taskIds.isEmpty()) {
                List<TaskDTO> tasks = taskServiceClient.getTasksByIds(taskIds, userId);
                if (tasks.size() != taskIds.size()) {
                    throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
                }
            }

            DocumentEntity doc = new DocumentEntity();
            doc.setName(file.getOriginalFilename());
            doc.setContentType(file.getContentType());
            doc.setSize(file.getSize());
            doc.setContent(file.getBytes());
            doc.setUploadDate(LocalDateTime.now());
            doc.setUserId(userId);
            
            if (taskIds != null && !taskIds.isEmpty()) {
                doc.setTaskIds(String.join(",", taskIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList())));
            }

            DocumentEntity savedDoc = documentRepository.save(doc);
            return new DocumentResponseDTO(savedDoc, baseUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }

    public DocumentResponseDTO getDocument(String id, String baseUrl) {
        return documentRepository.findById(id)
                .map(doc -> new DocumentResponseDTO(doc, baseUrl))
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
    }

    public byte[] getDocumentContent(String id) {
        return documentRepository.findById(id)
                .map(DocumentEntity::getContent)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
    }

    public List<DocumentResponseDTO> getAllDocuments(String baseUrl, String userId) {
        return documentRepository.findByUserId(userId).stream()
                .map(doc -> new DocumentResponseDTO(doc, baseUrl))
                .collect(Collectors.toList());
    }

    public List<DocumentResponseDTO> getDocumentsForTask(Long taskId, String userId) {
        return documentRepository.findByUserIdAndTaskIdsContaining(userId, taskId.toString()).stream()
                .map(doc -> new DocumentResponseDTO(doc, "")) // Base URL not needed for list view
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDocument(String id, String userId) {
        DocumentEntity doc = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
        
        if (!doc.getUserId().equals(userId)) {
            throw new DocumentNotFoundException("Document not found with id: " + id);
        }
        
        documentRepository.deleteById(id);
    }
}