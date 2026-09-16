package com.errday.splearn.application.enrollment.required;

import com.errday.splearn.domain.enrollment.Enrollment;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends Repository<Enrollment, Long> {
    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findByMemberId(Long memberId);

    Optional<Enrollment> findByMemberIdAndCourseId(Long memberId, Long courseId);
}
