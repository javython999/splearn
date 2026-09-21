package com.errday.splearn.application.course;

import com.errday.splearn.application.course.provided.CourseCreateRequest;
import com.errday.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.errday.splearn.application.course.provided.CourseValidator;
import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.application.course.required.CurriculumValidator;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.curriculum.InvalidCurriculumException;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.exception.ValidationException;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidationService implements CourseValidator {
    private final CourseRepository courseRepository;
    private final CurriculumValidator curriculumValidator;

    @Override
    public void validateForCreate(Instructor instructor, CourseCreateRequest request) throws ValidationException {
        instructor.ensureActive();

        List<String> errors = new ArrayList<>();

        checkTitleDuplicationForCreate(instructor, request.title(), errors);
        checkBannedWords(request.title(), errors);
        checkBannedWords(request.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForUpdate(Course course, CourseInfoUpdateRequest request) throws ValidationException {
        List<String> errors = new ArrayList<>();

        checkTitleDuplicationForUpdate(course, course.getInstructor(), request.title(), errors);
        checkBannedWords(request.title(), errors);
        checkBannedWords(request.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForReview(Course course) throws ValidationException {
        List<String> errors = new ArrayList<>();

        checkCurriculum(course, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForPublish(Course course) throws ValidationException {
        List<String> errors = new ArrayList<>();

        checkCurriculum(course, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForArchive(Course course) throws ValidationException {
        // TODO
    }

    private void checkTitleDuplicationForCreate(Instructor instructor, String title, List<String> errors) {
        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("이미 사용중인 강의 제목입니다. " + title);
        }
    }

    private void checkTitleDuplicationForUpdate(Course course, Instructor instructor, String title, List<String> errors) {
        courseRepository.findByInstructorAndTitle(instructor, title).ifPresent(found -> {
           if (!found.equals(course)) {
               errors.add("이미 사용중인 강의 제목입니다. " + title);
           }
        });
    }

    private void checkBannedWords(String text, List<String> errors) {
        // TODO: Implement banned words check
    }

    private void checkCurriculum(Course course, List<String> errors) {
        try {
            curriculumValidator.validate(course.getId());
        } catch (InvalidCurriculumException e) {
            errors.add(e.getMessage());
        }
    }


}
