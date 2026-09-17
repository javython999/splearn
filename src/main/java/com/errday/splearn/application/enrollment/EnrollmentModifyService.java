package com.errday.splearn.application.enrollment;

import com.errday.splearn.application.course.provided.CourseFinder;
import com.errday.splearn.application.enrollment.provided.Enroller;
import com.errday.splearn.application.enrollment.provided.EnrollRequest;
import com.errday.splearn.application.enrollment.provided.EnrollmentFinder;
import com.errday.splearn.application.enrollment.required.EnrollmentRepository;
import com.errday.splearn.application.member.provided.MemberFinder;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.enrollment.Enrollment;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class EnrollmentModifyService implements Enroller {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentFinder enrollmentFinder;
    private final MemberFinder memberFinder;
    private final CourseFinder courseFinder;

    @Override
    public Enrollment enroll(EnrollRequest request) {
        Member member = memberFinder.find(request.memberId());
        Course course = courseFinder.find(request.courseId());

        checkDuplication(member, course);

        Enrollment enrollment = Enrollment.enroll(member, course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment startStudying(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.startStudying();

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment complete(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.complete();

        return enrollmentRepository.save(enrollment);
    }

    private void checkDuplication(Member member, Course course) {
        if (enrollmentRepository.findByMemberIdAndCourseId(member.getId(), course.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 수강중인 강의입니다.");
        }
    }
}
