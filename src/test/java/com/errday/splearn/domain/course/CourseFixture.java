package com.errday.splearn.domain.course;

import com.errday.splearn.application.course.provided.CourseCreateRequest;
import com.errday.splearn.application.course.provided.CourseInfoUpdateRequest;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.instancio.Instancio;

import java.time.LocalDateTime;

import static org.instancio.Instancio.gen;
import static org.instancio.Select.field;

public class CourseFixture {
    public static Course createCourse(@Nullable Instructor instructor, @Nullable String title) {
        CourseDetail detail = Instancio.of(CourseDetail.class)
                .ignore(field(CourseDetail::getId))
                .generate(field(CourseDetail::getDescription), gen -> gen.string().maxLength(500).nullable())
                .set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
                .create();

        return Instancio.of(Course.class)
                .ignore(field(Course::getId))
                .set(field(Course::getInstructor),
                        instructor == null ? InstructorFixture.createActiveInstructor() : instructor)
                .set(field(Course::getTitle),
                        title == null ? gen().string().maxLength(100).minLength(2).get() : title)
                .set(field(Course::getStatus), CourseStatus.DRAFT)
                .set(field(Course::getDetail), detail)
                .create();
    }

    public static Course createCourse(Instructor instructor) {
        return createCourse(instructor, null);
    }

    public static Course createCourse() {
        return createCourse(null, null);
    }

    public static @Valid CourseCreateRequest createCoureCreateRequest(Long instructorId, @Nullable String title) {
        return Instancio.of(CourseCreateRequest.class)
                .set(field(CourseCreateRequest::instructorId), instructorId)
                .set(field(CourseCreateRequest::title),
                        title == null ? gen().string().maxLength(100).minLength(2).get() : title)
                .generate(field(CourseCreateRequest::description), gen -> gen.string().maxLength(500).nullable())
                .create();
    }

    public static CourseInfoUpdateRequest createCourseInfoForUpdateRequest(@Nullable String title) {
        return Instancio.of(CourseInfoUpdateRequest.class)
                .set(field(CourseInfoUpdateRequest::title), title == null ? gen().string().maxLength(100).minLength(2).get() : title)
                .generate(field(CourseInfoUpdateRequest::description), gen -> gen.string(). maxLength(500))
                .create();
    }

    public static Course createPublishedCourse() {
        Course course = createCourse();
        course.updateInfo(createCourseInfoForUpdateRequest(course.getTitle()).toInfo());
        course.submitForReview();
        course.publish();
        return course;
    }
}
