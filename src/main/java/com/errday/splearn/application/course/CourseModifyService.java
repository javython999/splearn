package com.errday.splearn.application.course;

import com.errday.splearn.application.course.provided.*;
import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.application.instructor.provided.InstructorFinder;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.exception.ValidationException;
import com.errday.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator, CoursePublisher {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final CourseValidator courseValidator;
    private final InstructorFinder instructorFinder;

    @Override
    public Course create(CourseCreateRequest request) throws ValidationException {
        Instructor instructor = instructorFinder.find(request.instructorId());

        courseValidator.validateForCreate(instructor, request);

        Course course = new Course(instructor, request.title(), request.description());

        return courseRepository.save(course);
    }

    @Override
    public Course updateInfo(Long courseId, CourseInfoUpdateRequest request) throws ValidationException {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForUpdate(course, request);

        course.updateInfo(request.toInfo());

        return courseRepository.save(course);
    }

    @Override
    public Course submitForReview(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForReview(course);

        course.submitForReview();

        return courseRepository.save(course);
    }

    @Override
    public Course publish(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForPublish(course);

        course.publish();

        return courseRepository.save(course);
    }

    @Override
    public Course archive(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForArchive(course);

        course.archive();

        return courseRepository.save(course);
    }
}
