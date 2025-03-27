package com.omnedu.videoservice.service;

import com.omnedu.videoservice.dto.VideoRequestDTO;
import com.omnedu.videoservice.model.Video;

import java.util.List;

public interface IVideoService {
    List<Video> getAllVideos();
    Video getVideoById(Long id);
    Video createVideo(VideoRequestDTO videoRequestDTO);
    Video updateVideo(Long id, VideoRequestDTO videoRequestDTO);
    void deleteVideo(Long id);
}