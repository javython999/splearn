package com.errday.splearn.application.course;

import com.errday.splearn.application.course.provided.CourseCreateRequest;
import com.errday.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.errday.splearn.application.course.provided.CourseValidator;
import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.exception.ValidationException;
import com.errday.splearn.support.stereotype.ApplicationService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidationService implements CourseValidator {
    private final CourseRepository courseRepository;

    @Override
    public void validateForCreate(Instructor instructor, CourseCreateRequest request) throws ValidationException {
        instructor.ensureActive();

        List<String> errors = new ArrayList<>();

        checkTitleDuplication(instructor, request.title(), errors);
        checkBannedWords(request.title(), errors);
        checkBannedWords(request.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void checkTitleDuplication(Instructor instructor, String title, List<String> errors) {
        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("이미 사용중인 강의 제목입니다. " + title);
        }
    }

    private void checkBannedWords(String text, List<String> errors) {
        // TODO: Implement banned words check
    }

    @Override
    public void validateForUpdate(Instructor instructor, CourseInfoUpdateRequest request) {

    }
}
