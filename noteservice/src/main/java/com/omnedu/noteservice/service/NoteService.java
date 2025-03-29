package com.omnedu.noteservice.service;

import com.omnedu.noteservice.client.TaskServiceClient;
import com.omnedu.noteservice.dto.NoteRequestDTO;
import com.omnedu.noteservice.dto.NoteResponseDTO;
import com.omnedu.noteservice.dto.TaskDTO;
import com.omnedu.noteservice.model.Note;
import com.omnedu.noteservice.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final TaskServiceClient taskServiceClient;

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

    @Transactional
    public NoteResponseDTO createNote(NoteRequestDTO noteRequestDTO, String userId) {
        // Validate tasks if they exist
        if (noteRequestDTO.getTaskIds() != null && !noteRequestDTO.getTaskIds().isEmpty()) {
            List<TaskDTO> tasks = taskServiceClient.getTasksByIds(noteRequestDTO.getTaskIds(), userId);
            if (tasks.size() != noteRequestDTO.getTaskIds().size()) {
                throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
            }
        }

        Note note = new Note();
        note.setTitle(noteRequestDTO.getTitle());
        note.setContent(noteRequestDTO.getContent());
        note.setUserId(userId);

        if (noteRequestDTO.getTaskIds() != null && !noteRequestDTO.getTaskIds().isEmpty()) {
            note.setTaskIds(String.join(",", noteRequestDTO.getTaskIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())));
        }

        Note savedNote = noteRepository.save(note);
        return new NoteResponseDTO(savedNote);
    }

    @Transactional
    public NoteResponseDTO updateNote(Long id, NoteRequestDTO noteRequestDTO, String userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found or not authorized"));

        // Validate tasks if they exist
        if (noteRequestDTO.getTaskIds() != null && !noteRequestDTO.getTaskIds().isEmpty()) {
            List<TaskDTO> tasks = taskServiceClient.getTasksByIds(noteRequestDTO.getTaskIds(), userId);
            if (tasks.size() != noteRequestDTO.getTaskIds().size()) {
                throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
            }
        }

        note.setTitle(noteRequestDTO.getTitle());
        note.setContent(noteRequestDTO.getContent());

        if (noteRequestDTO.getTaskIds() != null && !noteRequestDTO.getTaskIds().isEmpty()) {
            note.setTaskIds(String.join(",", noteRequestDTO.getTaskIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())));
        } else {
            note.setTaskIds(null);
        }

        Note updatedNote = noteRepository.save(note);
        return new NoteResponseDTO(updatedNote);
    }

    @Transactional
    public void deleteNote(Long id, String userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found or not authorized"));

        noteRepository.delete(note);
    }

    public List<NoteResponseDTO> getNotesForTask(Long taskId, String userId) {
        return noteRepository.findByUserIdAndTaskIdsContaining(userId, taskId.toString())
                .stream()
                .map(NoteResponseDTO::new)
                .collect(Collectors.toList());
    }
}