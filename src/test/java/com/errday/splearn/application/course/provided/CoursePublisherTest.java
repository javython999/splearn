package com.errday.splearn.application.course.provided;

import com.errday.splearn.domain.course.CourseStatus;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
public class CoursePublisherTest extends BaseApplicationServiceTest {
    final CoursePublisher coursePublisher;

    @BeforeEach
    void setUp() {
        prepareCourseWithCurriculum();
    }

    @Test
    void submitForReview() {
        var courseForReview = coursePublisher.submitForReview(course.getId());

        Assertions.assertThat(courseForReview.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    void publish() {
        coursePublisher.submitForReview(course.getId());

        var courseForPublish = coursePublisher.publish(course.getId());

        assertThat(courseForPublish.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
    }

    @Test
    void archive() {
        coursePublisher.submitForReview(course.getId());
        coursePublisher.publish(course.getId());
        var courseForArchive = coursePublisher.archive(course.getId());

        assertThat(courseForArchive.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
    }

}
