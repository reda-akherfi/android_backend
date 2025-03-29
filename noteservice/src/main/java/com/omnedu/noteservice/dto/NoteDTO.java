package com.omnedu.noteservice.dto;

import com.omnedu.noteservice.model.Note;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private String userId;
    private LocalDateTime createdAt;
    private List<Long> taskIds;

    // Constructor to convert Entity to DTO
    public NoteDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.userId = note.getUserId();
        this.createdAt = note.getCreatedAt();
    }

    // Empty constructor for frameworks that need it
    public NoteDTO() {
    }

    // Constructor for creating new notes
    public NoteDTO(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}