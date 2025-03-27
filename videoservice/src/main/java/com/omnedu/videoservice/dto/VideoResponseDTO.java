// VideoResponseDTO.java
package com.omnedu.videoservice.dto;

import com.omnedu.videoservice.model.Video;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VideoResponseDTO {
    private Long id;
    private String title;
    private String url;
    private LocalDateTime createdAt;

    // Constructor to convert Entity to DTO
    public VideoResponseDTO(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.url = video.getUrl();
        this.createdAt = video.getCreatedAt();
    }
}