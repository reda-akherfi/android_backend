package com.omnedu.noteservice.dto;

import com.omnedu.noteservice.model.Note;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NoteResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<Long> taskIds;

    // Constructor to convert Entity to DTO
    public NoteResponseDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();

        if (note.getTaskIds() != null && !note.getTaskIds().isEmpty()) {
            this.taskIds = Arrays.stream(note.getTaskIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }
}