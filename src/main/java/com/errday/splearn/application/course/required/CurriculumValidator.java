package com.errday.splearn.application.course.required;

import com.errday.splearn.domain.curriculum.InvalidCurriculumException;

public interface CurriculumValidator {
    void validate(Long courseId) throws InvalidCurriculumException;
}
