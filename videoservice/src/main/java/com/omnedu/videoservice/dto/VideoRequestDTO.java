package com.omnedu.videoservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class VideoRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "URL is required")
    private String url;
    
    private List<Long> taskIds;
}