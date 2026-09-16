package com.errday.splearn.domain.enrollment;

import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.member.Member;
import jakarta.annotation.Nullable;

import static com.errday.splearn.domain.course.CourseFixture.createPublishedCourse;
import static com.errday.splearn.domain.member.MemberFixture.createActiveMember;

public class EnrollmentFixture {

    public static Enrollment createEnrollment(@Nullable Member member, @Nullable Course course) {
        return Enrollment.enroll(
                member == null ? createActiveMember() : member,
                course == null? createPublishedCourse(): course
        );
    }

    public static Enrollment createEnrollment() {
        return createEnrollment(null, null);
    }
}
