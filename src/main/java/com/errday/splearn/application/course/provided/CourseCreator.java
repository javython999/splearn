package com.errday.splearn.application.course.provided;

import com.errday.splearn.domain.course.Course;
import jakarta.validation.Valid;

/**
 * 강의를 준비하는 작업
 */
public interface CourseCreator {
    Course create(@Valid CourseCreateRequest request);

    Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest request);
}
