package com.omnedu.noteservice.controller;

import com.omnedu.noteservice.dto.NoteRequestDTO;
import com.omnedu.noteservice.dto.NoteResponseDTO;
import com.omnedu.noteservice.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final HttpServletRequest request;

    @GetMapping
    public List<NoteResponseDTO> getAllNotes() {
        String userId = (String) request.getAttribute("X-User-Id");
        return noteService.getAllNotesByUserId(userId);
    }

    @GetMapping("/{id}")
    public NoteResponseDTO getNoteById(@PathVariable Long id) {
        String userId = (String) request.getAttribute("X-User-Id");
        return noteService.getNoteByIdAndUserId(id, userId);
    }

    @GetMapping("/task/{taskId}")
    public List<NoteResponseDTO> getNotesForTask(@PathVariable Long taskId) {
        String userId = (String) request.getAttribute("X-User-Id");
        return noteService.getNotesForTask(taskId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponseDTO createNote(@RequestBody NoteRequestDTO noteRequestDTO) {
        String userId = (String) request.getAttribute("X-User-Id");
        return noteService.createNote(noteRequestDTO, userId);
    }

    @PutMapping("/{id}")
    public NoteResponseDTO updateNote(@PathVariable Long id, @RequestBody NoteRequestDTO noteRequestDTO) {
        String userId = (String) request.getAttribute("X-User-Id");
        return noteService.updateNote(id, noteRequestDTO, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id) {
        String userId = (String) request.getAttribute("X-User-Id");
        noteService.deleteNote(id, userId);
    }
}