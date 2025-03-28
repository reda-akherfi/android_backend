package com.omnedu.videoservice.dto;

import com.omnedu.videoservice.model.Video;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VideoResponseDTO {
    private Long id;
    private String title;
    private String url;
    private String userId;
    private List<Long> taskIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public VideoResponseDTO(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.url = video.getUrl();
        this.userId = video.getUserId();
        this.createdAt = video.getCreatedAt();
        this.updatedAt = video.getUpdatedAt();
        
        if (video.getTaskIds() != null && !video.getTaskIds().isEmpty()) {
            this.taskIds = Arrays.stream(video.getTaskIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }
}