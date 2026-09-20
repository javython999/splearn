package com.errday.splearn.application.course.provided;

import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
    final CourseCreator courseCreator;

    @Test
    void create() {
        prepareInstructor();

        Course course = courseCreator.create(CourseFixture.createCoureCreateRequest(instructor.getId(), null));

        assertThat(course).isNotNull();
    }

    @Test
    void updateInfo() {
        prepareInstructor();
        Course course = courseCreator.create(CourseFixture.createCoureCreateRequest(instructor.getId(), null));

        Course updated = courseCreator.updateInfo(course.getId(), CourseFixture.createCourseInfoForUpdateRequest("Updated"));

        assertThat(updated.getTitle()).isEqualTo("Updated");
    }
}