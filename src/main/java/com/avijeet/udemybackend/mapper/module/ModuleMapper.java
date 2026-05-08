package com.avijeet.udemybackend.mapper.module;

import com.avijeet.udemybackend.dto.module.ModuleRequestDto;
import com.avijeet.udemybackend.dto.module.ModuleResponseDto;
import com.avijeet.udemybackend.entities.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper {
    @Mapping(target = "id", ignore = true)
    Module moduleDtoToModule(ModuleRequestDto requestDto);

    @Mapping(source = "id", target = "moduleId")
    @Mapping(source = "course.id", target = "courseId")
    ModuleResponseDto moduleToModuleResponseDto(Module module);
}
