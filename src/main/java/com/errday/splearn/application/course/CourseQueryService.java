package com.errday.splearn.application.course;

import com.errday.splearn.application.course.provided.CourseFinder;
import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class CourseQueryService implements CourseFinder {
    private final CourseRepository courseRepository;

    @Override
    public Course find(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("강의를 찾을 수 없습니다. ID: " + courseId)
        );
    }

    @Override
    public List<Course> findByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    @Override
    public List<Course> findByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }
}
