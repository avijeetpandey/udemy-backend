package com.avijeet.udemybackend.controllers.module;

import com.avijeet.udemybackend.dto.module.ModuleRequestDto;
import com.avijeet.udemybackend.dto.module.ModuleResponseDto;
import com.avijeet.udemybackend.service.module.ModuleService;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import com.avijeet.udemybackend.utils.api.ApiRoutes;
import com.avijeet.udemybackend.utils.api.BaseController;
import com.avijeet.udemybackend.utils.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.MODULE_END_POINT)
@RequiredArgsConstructor
public class ModuleController extends BaseController  {
    private final ModuleService moduleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ModuleResponseDto>> create(@RequestBody ModuleRequestDto moduleRequestDto) {
        ModuleResponseDto moduleResponseDto = moduleService.createModule(moduleRequestDto);
        return ok(ApiConstants.DONE_MESSAGE, moduleResponseDto);
    }
}
