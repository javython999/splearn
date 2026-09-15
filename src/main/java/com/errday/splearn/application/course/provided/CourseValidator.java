package com.errday.splearn.application.course.provided;

import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.exception.ValidationException;

public interface CourseValidator {
    void validateForCreate(Instructor instructor, CourseCreateRequest request) throws ValidationException;

    void validateForUpdate(Course course, CourseInfoUpdateRequest request) throws ValidationException;

    void validateForReview(Course course) throws ValidationException;

    void validateForPublish(Course course) throws ValidationException;

    void validateForArchive(Course course) throws ValidationException;
}
