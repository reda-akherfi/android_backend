package com.omnedu.document_service.controller;

import com.omnedu.document_service.dto.DocumentResponseDTO;
import com.omnedu.document_service.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final HttpServletRequest request;

    @PostMapping
    public ResponseEntity<DocumentResponseDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "taskIds", required = false) List<Long> taskIds,
            @RequestHeader("X-User-Id") String userId) {
        DocumentResponseDTO response = documentService.store(file, getBaseUrl(), userId, taskIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadDocument(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        DocumentResponseDTO doc = documentService.getDocument(id, getBaseUrl());
        if (!doc.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .body(new ByteArrayResource(documentService.getDocumentContent(id)));
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getUserDocuments(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(documentService.getAllDocuments(getBaseUrl(), userId));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsForTask(
            @PathVariable Long taskId,
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(documentService.getDocumentsForTask(taskId, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        documentService.deleteDocument(id, userId);
        return ResponseEntity.noContent().build();
    }

    private String getBaseUrl() {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}