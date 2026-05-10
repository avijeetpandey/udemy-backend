package com.avijeet.udemybackend.controllers.video;

import com.avijeet.udemybackend.dto.video.VideoRequestDto;
import com.avijeet.udemybackend.dto.video.VideoResponseDto;
import com.avijeet.udemybackend.service.video.VideoService;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import com.avijeet.udemybackend.utils.api.ApiRoutes;
import com.avijeet.udemybackend.utils.api.BaseController;
import com.avijeet.udemybackend.utils.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.VIDEO_END_POINT)
public class VideoController extends BaseController {
    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<VideoResponseDto>> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestPart("videoRequestDto") VideoRequestDto videoRequestDto) {
        VideoResponseDto videoResponseDto = videoService.initVideo(videoRequestDto, file);
        return ok(ApiConstants.DONE_MESSAGE, videoResponseDto);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<ApiResponse<VideoResponseDto>> getVideo(@PathVariable Long videoId) {
        VideoResponseDto videoResponseDto = videoService.getVideo(videoId);
        return ok(ApiConstants.DONE_MESSAGE, videoResponseDto);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ok(ApiConstants.DONE_MESSAGE, null);
    }
}
