package com.omnedu.videoservice.controller;

import com.omnedu.videoservice.dto.VideoRequestDTO;
import com.omnedu.videoservice.dto.VideoResponseDTO;
import com.omnedu.videoservice.service.VideoServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoServiceImpl videoService;

    @GetMapping
    public List<VideoResponseDTO> getAllVideos() {
        return videoService.getAllVideos().stream()
                .map(VideoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VideoResponseDTO getVideoById(@PathVariable Long id) {
        return new VideoResponseDTO(videoService.getVideoById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideoResponseDTO createVideo(@RequestBody VideoRequestDTO videoRequestDTO) {
        return new VideoResponseDTO(videoService.createVideo(videoRequestDTO));
    }

    @PutMapping("/{id}")
    public VideoResponseDTO updateVideo(@PathVariable Long id, @RequestBody VideoRequestDTO videoRequestDTO) {
        return new VideoResponseDTO(videoService.updateVideo(id, videoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}