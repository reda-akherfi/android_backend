package com.omnedu.videoservice.repository;

import com.omnedu.videoservice.model.Video;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserIdAndTaskIdsContaining(String userId, String taskId);
}