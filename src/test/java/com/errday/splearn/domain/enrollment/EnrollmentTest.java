package com.errday.splearn.domain.enrollment;

import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EnrollmentTest {

    @Test
    void enroll() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createPublishedCourse();

        Enrollment enrollment = Enrollment.enroll(member, course);

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getEnrolledAt()).isNotNull();
    }

    @Test
    void enrollFailNotPublishedCourse() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createCourse();

        assertThatThrownBy(() -> Enrollment.enroll(member, course))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();

        enrollment.startStudying();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);

        assertThatThrownBy(enrollment::startStudying)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complete() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.startStudying();

        enrollment.complete();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(enrollment.getCompletedAt()).isNotNull();

        assertThatThrownBy(enrollment::complete)
                .isInstanceOf(IllegalStateException.class);
    }
}