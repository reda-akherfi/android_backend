package com.omnedu.document_service.service;

import com.omnedu.document_service.dto.DocumentResponseDTO;
import com.omnedu.document_service.exception.DocumentNotFoundException;
import com.omnedu.document_service.model.DocumentEntity;
import com.omnedu.document_service.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentResponseDTO store(MultipartFile file, String baseUrl) {
        try {
            DocumentEntity doc = new DocumentEntity();
            doc.setName(file.getOriginalFilename());
            doc.setContentType(file.getContentType());
            doc.setSize(file.getSize());
            doc.setContent(file.getBytes());
            doc.setUploadDate(LocalDateTime.now());
            
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

    public List<DocumentResponseDTO> getAllDocuments(String baseUrl) {
        return documentRepository.findAll().stream()
                .map(doc -> new DocumentResponseDTO(doc, baseUrl))
                .collect(Collectors.toList());
    }

    public void deleteDocument(String id) {
        if (!documentRepository.existsById(id)) {
            throw new DocumentNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }
}