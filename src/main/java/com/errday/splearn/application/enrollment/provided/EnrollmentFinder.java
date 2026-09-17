package com.errday.splearn.application.enrollment.provided;

import com.errday.splearn.domain.enrollment.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentFinder {
    Enrollment find(Long enrollmentId);

    List<Enrollment> findByMemberId(Long memberId);

    Optional<Enrollment> findByMemberAndCourse(Long memberId, Long courseId);
}
