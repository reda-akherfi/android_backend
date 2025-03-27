package com.omnedu.videoservice.repository;

import com.omnedu.videoservice.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}