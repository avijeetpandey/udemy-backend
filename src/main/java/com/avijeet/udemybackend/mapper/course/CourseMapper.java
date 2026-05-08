package com.avijeet.udemybackend.mapper.course;

import com.avijeet.udemybackend.dto.course.CourseRequestDto;
import com.avijeet.udemybackend.dto.course.CourseResponseDto;
import com.avijeet.udemybackend.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    Course courseDtoToCourse(CourseRequestDto requestDto);

    @Mapping(target = "courseId", source = "id")
    CourseResponseDto courseToCourseResponseDto(Course course);
}
