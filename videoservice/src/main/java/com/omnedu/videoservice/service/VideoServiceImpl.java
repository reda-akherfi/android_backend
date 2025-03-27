package com.omnedu.videoservice.service;

import com.omnedu.videoservice.dto.VideoRequestDTO;
import com.omnedu.videoservice.model.Video;
import com.omnedu.videoservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements IVideoService {
    private final VideoRepository videoRepository;

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    @Override
    public Video createVideo(VideoRequestDTO videoRequestDTO) {
        Video video = new Video();
        video.setTitle(videoRequestDTO.getTitle());
        video.setUrl(videoRequestDTO.getUrl());
        return videoRepository.save(video);
    }

    @Override
    public Video updateVideo(Long id, VideoRequestDTO videoRequestDTO) {
        Video video = getVideoById(id);
        video.setTitle(videoRequestDTO.getTitle());
        video.setUrl(videoRequestDTO.getUrl());
        return videoRepository.save(video);
    }

    @Override
    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
}