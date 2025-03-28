package com.omnedu.videoservice.service;

import com.omnedu.videoservice.client.TaskServiceClient;
import com.omnedu.videoservice.dto.TaskDTO;
import com.omnedu.videoservice.dto.VideoRequestDTO;
import com.omnedu.videoservice.model.Video;
import com.omnedu.videoservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final TaskServiceClient taskServiceClient;

    
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    
    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    
    @Transactional
    public Video createVideo(VideoRequestDTO videoRequestDTO, String userId) {
        // Validate tasks exist and belong to user
        if (videoRequestDTO.getTaskIds() != null && !videoRequestDTO.getTaskIds().isEmpty()) {
            List<TaskDTO> tasks = taskServiceClient.getTasksByIds(videoRequestDTO.getTaskIds(), userId);
            if (tasks.size() != videoRequestDTO.getTaskIds().size()) {
                throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
            }
        }

        Video video = new Video();
        video.setTitle(videoRequestDTO.getTitle());
        video.setUrl(videoRequestDTO.getUrl());
        video.setUserId(userId);
        
        if (videoRequestDTO.getTaskIds() != null && !videoRequestDTO.getTaskIds().isEmpty()) {
            video.setTaskIds(String.join(",", videoRequestDTO.getTaskIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())));
        }

        return videoRepository.save(video);
    }

    
    @Transactional
    public Video updateVideo(Long id, VideoRequestDTO videoRequestDTO, String userId) {
        Video video = getVideoById(id);
        
        if (!video.getUserId().equals(userId)) {
            throw new RuntimeException("Video not found");
        }

        // Validate tasks exist and belong to user
        if (videoRequestDTO.getTaskIds() != null && !videoRequestDTO.getTaskIds().isEmpty()) {
            List<TaskDTO> tasks = taskServiceClient.getTasksByIds(videoRequestDTO.getTaskIds(), userId);
            if (tasks.size() != videoRequestDTO.getTaskIds().size()) {
                throw new IllegalArgumentException("One or more tasks not found or don't belong to user");
            }
        }

        video.setTitle(videoRequestDTO.getTitle());
        video.setUrl(videoRequestDTO.getUrl());
        
        if (videoRequestDTO.getTaskIds() != null && !videoRequestDTO.getTaskIds().isEmpty()) {
            video.setTaskIds(String.join(",", videoRequestDTO.getTaskIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())));
        } else {
            video.setTaskIds(null);
        }

        return videoRepository.save(video);
    }

    
    @Transactional
    public void deleteVideo(Long id, String userId) {
        Video video = getVideoById(id);
        
        if (!video.getUserId().equals(userId)) {
            throw new RuntimeException("Video not found");
        }

        videoRepository.delete(video);
    }

    public List<Video> getVideosForTask(Long taskId, String userId) {
        return videoRepository.findByUserIdAndTaskIdsContaining(userId, taskId.toString());
    }
}