package com.omnedu.document_service.service;

import com.omnedu.document_service.model.DocumentEntity;
import com.omnedu.document_service.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public DocumentEntity store(MultipartFile file) throws IOException {
        DocumentEntity doc = new DocumentEntity();
        doc.setName(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setSize(file.getSize());
        doc.setContent(file.getBytes());
        return documentRepository.save(doc);
    }

    public DocumentEntity getDocument(String id) {
        return documentRepository.findById(id).orElse(null);
    }

    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    public void deleteDocument(String id) {
        documentRepository.deleteById(id);
    }
}