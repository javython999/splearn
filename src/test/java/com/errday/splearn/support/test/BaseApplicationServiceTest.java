package com.errday.splearn.support.test;

import com.errday.splearn.application.course.provided.CourseCreator;
import com.errday.splearn.application.curriculum.provided.CurriculumFinder;
import com.errday.splearn.application.enrollment.provided.EnrollRequest;
import com.errday.splearn.application.enrollment.provided.Enroller;
import com.errday.splearn.application.instructor.provided.InstructorApplication;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.enrollment.Enrollment;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;


@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    @Autowired
    CourseCreator courseCreator;

    @Autowired
    Enroller enroller;

    @Autowired
    CurriculumFinder curriculumFinder;

    protected Member member;
    protected Instructor instructor;
    protected Course course;
    protected Enrollment enrollment;
    protected Curriculum curriculum;


    @NonNull
    protected Instructor prepareInstructor() {
        prepareActiveMember();

        instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        instructor.approve();

        return instructor;
    }

    protected @NonNull Member prepareActiveMember() {
        member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        member.activate();

        return member;
    }

    protected Course prepareCourse() {
        prepareInstructor();

        course = courseCreator.create(CourseFixture.createCoureCreateRequest(instructor.getId(), null));
        course.updateInfo(CourseFixture.createCourseInfoForUpdateRequest(null).toInfo());

        return course;
    }

    protected Course preparePublishedCourse() {
        prepareCourse();

        course.submitForReview();
        course.publish();

        return course;
    }

    protected Enrollment prepareEnrollment() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        enrollment = enroller.enroll(new EnrollRequest(member.getId(), course.getId()));
        return enrollment;
    }

    protected Curriculum prepareCurriculumSectionsAndLessons(Course course) {
        Curriculum curriculum = curriculumFinder.findByCourseId(course.getId());

        curriculum.addSection("S0");
        curriculum.addLesson(0, "L0");
        curriculum.addLesson(0, "L1");

        curriculum.addSection("S1");
        curriculum.addLesson(1, "L2");
        curriculum.addLesson(1, "L3");

        curriculum.addSection("S2");
        curriculum.addLesson(2, "L4");

        this.curriculum = curriculum;

        return this.curriculum;
    }

    protected Course prepareCourseWithCurriculum() {
        prepareCourse();
        prepareCurriculumSectionsAndLessons(course);

        return this.course;
    }
}
