package com.avijeet.udemybackend.service.module;

import com.avijeet.udemybackend.dto.module.ModuleRequestDto;
import com.avijeet.udemybackend.dto.module.ModuleResponseDto;
import com.avijeet.udemybackend.entities.Course;
import com.avijeet.udemybackend.entities.Module;
import com.avijeet.udemybackend.exceptions.course.CourseNotFoundException;
import com.avijeet.udemybackend.exceptions.module.ModuleCreationFailed;
import com.avijeet.udemybackend.mapper.module.ModuleMapper;
import com.avijeet.udemybackend.repository.course.CourseRepository;
import com.avijeet.udemybackend.repository.module.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    @Transactional
    public ModuleResponseDto createModule(ModuleRequestDto moduleRequestDto) {
        try {
            Course course = courseRepository.findById(moduleRequestDto.courseId()).orElseThrow(() -> {
                log.error("Course with id {} : not found", moduleRequestDto.courseId());
                return new CourseNotFoundException("Course not found with id: " + moduleRequestDto.courseId());
            });

            Module module = moduleMapper.moduleDtoToModule(moduleRequestDto);
            module.setCourse(course);
            moduleRepository.save(module);

            return moduleMapper.moduleToModuleResponseDto(module);
        } catch (Exception e) {
            throw new ModuleCreationFailed("Failed to create module" + e.getLocalizedMessage());
        }
    }
}
