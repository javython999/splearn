package com.errday.splearn.support.test;

import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.application.enrollment.required.EnrollmentRepository;
import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.domain.enrollment.Enrollment;
import com.errday.splearn.domain.enrollment.EnrollmentFixture;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BaseRepositoryTest {
    @Autowired
    protected EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    protected Member member;
    protected Instructor instructor;
    protected Course course;
    protected Enrollment enrollment;

    protected Course preparePublishedCourse() {
        prepareCourse();

        course = courseRepository.save(CourseFixture.createCourse(instructor, null));
        course.updateInfo(CourseFixture.createCourseInfoForUpdateRequest(null).toInfo());
        course.submitForReview();
        course.publish();

        return course;
    }

    protected Course prepareCourse() {
        return prepareCourse(null, null);
    }

    protected Course prepareCourse(@Nullable Instructor instructor, @Nullable String title) {
        if (instructor == null) {
            prepareActiveInstructor();
        }

        course = courseRepository.save(CourseFixture.createCourse(
                instructor == null ? this.instructor : instructor, title));

        course.updateInfo(CourseFixture.createCourseInfoForUpdateRequest(title).toInfo());

        return course;
    }


    protected Instructor prepareActiveInstructor() {
        prepareActiveMember();

        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));

        return instructor;
    }

    protected Instructor prepareActiveInstructor(Member member) {
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));

        return instructor;
    }

    protected Member prepareActiveMember() {
        member = memberRepository.save(MemberFixture.createActiveMember());

        return member;
    }

    protected Enrollment prepareEnrollment(Member member, Course course) {
        enrollment = enrollmentRepository.save(EnrollmentFixture.createEnrollment(member, course));
        return enrollment;
    }
}
