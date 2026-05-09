package com.avijeet.udemybackend.service.video;

import com.avijeet.udemybackend.dto.video.VideoRequestDto;
import com.avijeet.udemybackend.dto.video.VideoResponseDto;
import com.avijeet.udemybackend.entities.Module;
import com.avijeet.udemybackend.entities.Video;
import com.avijeet.udemybackend.exceptions.module.ModuleNotFoundException;
import com.avijeet.udemybackend.mapper.video.VideoMapper;
import com.avijeet.udemybackend.repository.course.CourseRepository;
import com.avijeet.udemybackend.repository.module.ModuleRepository;
import com.avijeet.udemybackend.repository.video.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoUploadService videoUploadService;
    private final VideoRepository videoRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final VideoMapper videoMapper;

    @Transactional
    public VideoResponseDto initVideo(VideoRequestDto videoRequestDto, MultipartFile file) {
        log.info("Initializing video upload for module id: {}", videoRequestDto.moduleId());

        if (file == null || file.isEmpty()) {
            log.error("Video file is null or empty");
            throw new IllegalArgumentException("Video file cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            log.warn("Invalid file type uploaded. Expected a video but got: {}", contentType);
            throw new IllegalArgumentException("Only video files are allowed");
        }

        Module module = moduleRepository.findById(videoRequestDto.moduleId()).orElseThrow(() -> {
            log.error("Module with id {} not found", videoRequestDto.moduleId());
            return new ModuleNotFoundException("Module not found with id: " + videoRequestDto.moduleId());
        });

        String videoUrl;
        try {
            videoUrl = videoUploadService.uploadAndGetUrl(file, module.getTitle());
            log.info("Successfully uploaded video to storage. URL: {}", videoUrl);
        } catch (Exception e) {
            log.error("Failed to upload video to storage: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload video file to storage", e);
        }

        try {
            Video video = videoMapper.videoDtoToVideo(videoRequestDto);
            video.setMinioUrl(videoUrl);
            video.setModule(module);

            Video savedVideo = videoRepository.save(video);
            log.info("Successfully saved video record with ID: {}", savedVideo.getId());

            return videoMapper.videoToVideoResponseDto(savedVideo);
        } catch (Exception e) {
            log.error("Failed to save video entity to the database for module id {}: {}", videoRequestDto.moduleId(), e.getMessage(), e);
            throw new RuntimeException("An error occurred while saving the video record", e);
        }
    }

    @Transactional(readOnly = true)
    public VideoResponseDto getVideo(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> {
            log.error("Video with id {} not found", videoId);
            return new RuntimeException("Video with id" + videoId + "not found");
        });
        return videoMapper.videoToVideoResponseDto(video);
    }
}
