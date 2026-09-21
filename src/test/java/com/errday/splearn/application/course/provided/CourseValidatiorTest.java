package com.errday.splearn.application.course.provided;

import com.errday.splearn.application.course.required.CourseRepository;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.support.exception.ValidationException;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseValidatiorTest extends BaseApplicationServiceTest {
    final CourseValidator courseValidator;
    final CourseRepository courseRepository;
    final CoursePublisher coursePublisher;

    @Test
    void titleDuplicationForCreate() {
        var instructor1 = prepareInstructor();
        var instructor2 = prepareInstructor();

        courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Spring"));
        courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Code"));

        // instructor1 중복되지 않는 제목 pass
        courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Spring 7", null));

        // instructor1 중복 제목 fail
        assertThatThrownBy(() -> courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Clean Spring", null)))
                .isInstanceOfSatisfying(ValidationException.class, error -> {
                    assertThat(error.getErrors()).hasSize(1);
                });
        
        // instructor2 instructor1과 중복되는 제목 pass
        courseValidator.validateForCreate(instructor2, new CourseCreateRequest(instructor2.getId(), "Clean Spring", null));
    }

    @Test
    void titleDuplicationForUpdate() {
        var instructor1 = prepareInstructor();
        var instructor2 = prepareInstructor();

        Course course1_1 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Spring"));
        Course course1_2 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Code"));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Spring"));

        // title 변경 없이 update pass
        courseValidator.validateForUpdate(course1_1, CourseFixture.createCourseInfoForUpdateRequest(course1_1.getTitle()));

        // title 변경시 중복
        assertThatThrownBy(() -> courseValidator.validateForUpdate(course1_1, CourseFixture.createCourseInfoForUpdateRequest(course1_2.getTitle())))
                .isInstanceOfSatisfying(ValidationException.class, e -> {
                    assertThat(e.getErrors()).hasSize(1);
                });
    }

    @Test
    void submitForReviewFailInvalidCurriculum() {
        Course course = prepareCourse();
        Curriculum curriculum = prepareCurriculumSectionsAndLessons(course);
        curriculum.removeLesson(2, 0);

        assertThatThrownBy(() -> coursePublisher.submitForReview(course.getId()))
                        .isInstanceOf(ValidationException.class);
    }

    @Test
    void publishFailInvalidCurriculum() {
        Course course = prepareCourse();
        Curriculum curriculum = prepareCurriculumSectionsAndLessons(course);
        coursePublisher.submitForReview(course.getId());
        curriculum.removeLesson(2, 0);

        assertThatThrownBy(() -> coursePublisher.publish(course.getId()))
                .isInstanceOf(ValidationException.class);
    }

}