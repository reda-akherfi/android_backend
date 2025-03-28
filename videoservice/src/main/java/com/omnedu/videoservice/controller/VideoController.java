package com.omnedu.videoservice.controller;

import com.omnedu.videoservice.dto.VideoRequestDTO;
import com.omnedu.videoservice.dto.VideoResponseDTO;
import com.omnedu.videoservice.service.VideoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideoResponseDTO createVideo(
            @RequestBody @Valid VideoRequestDTO videoRequestDTO,
            @RequestHeader("X-User-Id") String userId) {
        return new VideoResponseDTO(videoService.createVideo(videoRequestDTO, userId));
    }

    @GetMapping("/{id}")
    public VideoResponseDTO getVideoById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        return new VideoResponseDTO(videoService.getVideoById(id));
    }

    @GetMapping
    public List<VideoResponseDTO> getUserVideos(
            @RequestHeader("X-User-Id") String userId) {
        return videoService.getAllVideos().stream()
                .filter(v -> v.getUserId().equals(userId))
                .map(VideoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/task/{taskId}")
    public List<VideoResponseDTO> getVideosForTask(
            @PathVariable Long taskId,
            @RequestHeader("X-User-Id") String userId) {
        return videoService.getVideosForTask(taskId, userId).stream()
                .map(VideoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public VideoResponseDTO updateVideo(
            @PathVariable Long id,
            @RequestBody @Valid VideoRequestDTO videoRequestDTO,
            @RequestHeader("X-User-Id") String userId) {
        return new VideoResponseDTO(videoService.updateVideo(id, videoRequestDTO, userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        videoService.deleteVideo(id, userId);
    }
}