package com.errday.splearn.application.course.provided;

import com.errday.splearn.domain.course.Course;

import java.util.List;

/**
 * 강의를 조회
 */
public interface CourseFinder {
    Course find(Long courseId);

    List<Course> findByTitle(String keyword);

    List<Course> findByInstructor(Long instructorId);
}
