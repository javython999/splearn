package com.errday.splearn.application.enrollment.provided;

import com.errday.splearn.domain.enrollment.Enrollment;

public interface Enroller {
    Enrollment enroll(Long memberId, Long courseId);

    Enrollment startStudying(Long enrollmentId);

    Enrollment complete(Long enrollmentId);
}
