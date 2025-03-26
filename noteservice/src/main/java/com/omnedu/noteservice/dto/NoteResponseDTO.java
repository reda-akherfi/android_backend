package com.omnedu.noteservice.dto;

import com.omnedu.noteservice.model.Note;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    // Constructor to convert Entity to DTO
    public NoteResponseDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();
    }
}