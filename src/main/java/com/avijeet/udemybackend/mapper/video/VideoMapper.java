package com.avijeet.udemybackend.mapper.video;

import com.avijeet.udemybackend.dto.video.VideoRequestDto;
import com.avijeet.udemybackend.dto.video.VideoResponseDto;
import com.avijeet.udemybackend.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "moduleId", target = "module.id")
    Video videoDtoToVideo(VideoRequestDto requestDto);

    @Mapping(source = "id", target = "videoId")
    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "minioUrl", target = "videoUrl")
    VideoResponseDto videoToVideoResponseDto(Video video);
}
