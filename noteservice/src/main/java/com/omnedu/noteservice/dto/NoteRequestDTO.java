package com.omnedu.noteservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestDTO {
    private String title;
    private String content;
    private List<Long> taskIds;
}