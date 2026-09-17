package com.errday.splearn.application.enrollment.provided;

import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.enrollment.Enrollment;
import com.errday.splearn.domain.enrollment.EnrollmentStatus;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class EnrollerTest extends BaseApplicationServiceTest {
    final Enroller enroller;

    @Test
    void enroll() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enrollment = enroller.enroll(new EnrollRequest(member.getId(), course.getId()));

        assertThat(enrollment.getId()).isNotNull();
    }

    @Test
    void enrollFailDuplicate() {
        prepareEnrollment();

        assertThatThrownBy(() -> enroller.enroll(new EnrollRequest(enrollment.getMember().getId(), enrollment.getCourse().getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void enrollFailNullIds() {
        assertThatThrownBy(() -> enroller.enroll(new EnrollRequest(null, null)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = prepareEnrollment();

        enroller.startStudying(enrollment.getId());

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);
    }

    @Test
    void complete() {
        Enrollment enrollment = prepareEnrollment();
        enroller.startStudying(enrollment.getId());

        Enrollment enrollmentCompleted = enroller.complete(enrollment.getId());

        assertThat(enrollmentCompleted.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
    }
}