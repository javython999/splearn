package com.errday.splearn.application.instructor.provided;

import com.errday.splearn.domain.instructor.Instructor;
import jakarta.validation.Valid;

/**
 * 강사 신청
 */
public interface InstructorApplication {
    Instructor apply(@Valid InstructorApplyRequest request);

    Instructor approve(Long instructorId);

    Instructor reject(Long instructorId);
}
