package com.omnedu.noteservice.service;

import com.omnedu.noteservice.dto.NoteRequestDTO;
import com.omnedu.noteservice.dto.NoteResponseDTO;
import com.omnedu.noteservice.model.Note;
import com.omnedu.noteservice.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<NoteResponseDTO> getAllNotesByUserId(String userId) {
        return noteRepository.findByUserId(userId)
                .stream()
                .map(NoteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public NoteResponseDTO getNoteByIdAndUserId(Long id, String userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found or not authorized"));
        return new NoteResponseDTO(note);
    }

    public NoteResponseDTO createNote(NoteRequestDTO noteRequestDTO, String userId) {
        Note note = new Note();
        note.setTitle(noteRequestDTO.getTitle());
        note.setContent(noteRequestDTO.getContent());
        note.setUserId(userId);
        
        Note savedNote = noteRepository.save(note);
        return new NoteResponseDTO(savedNote);
    }

    public NoteResponseDTO updateNote(Long id, NoteRequestDTO noteRequestDTO, String userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found or not authorized"));
        
        note.setTitle(noteRequestDTO.getTitle());
        note.setContent(noteRequestDTO.getContent());
        
        Note updatedNote = noteRepository.save(note);
        return new NoteResponseDTO(updatedNote);
    }

    public void deleteNote(Long id, String userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found or not authorized"));
        
        noteRepository.delete(note);
    }
}