package com.errday.splearn.application.course;

import com.errday.splearn.application.course.provided.CourseCreateRequest;
import com.errday.splearn.application.course.provided.CourseCreator;
import com.errday.splearn.application.course.provided.CourseFinder;
import com.errday.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.application.instructor.provided.InstructorFinder;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final InstructorFinder instructorFinder;

    @Override
    public Course create(CourseCreateRequest request) {
        Instructor instructor = instructorFinder.find(request.instructorId());

        // 2. validate request
        // 3. save

        return null;
    }

    @Override
    public Course updateInfo(Long courseId, CourseInfoUpdateRequest request) {
        return null;
    }
}
