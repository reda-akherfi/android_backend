package com.omnedu.noteservice.repository;

import com.omnedu.noteservice.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(String userId);
    Optional<Note> findByIdAndUserId(Long id, String userId);
}