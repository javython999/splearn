package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.course.CourseFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CurriculumTest {
    @Test
    void create() {
        var course = CourseFixture.createCourse();

        Curriculum curriculum = new Curriculum(course);

        assertThat(curriculum.getCourse()).isEqualTo(course);

        assertThatThrownBy(() -> new Curriculum(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void addSection() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        Section section =  curriculum.addSection("Section 1");

        assertThat(curriculum.getSections()).containsExactly(section);
        assertThat(curriculum.getSections()).extracting(Section::getTitle)
                .containsExactly("Section 1");
        assertThatThrownBy(() -> curriculum.addSection(null))
                .isInstanceOf(NullPointerException.class);
    }


    @Test
    void addSectionWithIndex() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        Section section1 = curriculum.addSection("S1");
        Section section2 = curriculum.addSection("S2");

        assertThat(curriculum.getSections()).containsExactly(section1, section2);

        Section section1_1 = curriculum.addSection(1, "S1_1");

        assertThat(curriculum.getSections()).containsExactly(section1, section1_1, section2);

        Section secttion3 = curriculum.addSection(3, "S3");

        assertThat(curriculum.getSections()).containsExactly(section1, section1_1, section2, secttion3);

        assertThatThrownBy(() -> curriculum.addSection(5, "S5"))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void addLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section section0 = curriculum.addSection("S0");
        Section section1 = curriculum.addSection("S1");

        Lesson lesson0 = curriculum.addLesson(0,"L0");
        Lesson lesson1 = curriculum.addLesson(0,"L1");

        assertThat(section0.getLessons()).containsExactly(lesson0, lesson1);

        Lesson lesson2 = curriculum.addLesson(1, "L2");

        assertThat(section1.getLessons()).containsExactly(lesson2);
    }

    @Test
    void updateSectionTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section section0 = curriculum.addSection("S0");
        Section section1 = curriculum.addSection("S1");

        curriculum.updateSectionTitle(0, "S0 Updated");

        assertThat(section0.getTitle()).isEqualTo("S0 Updated");

        curriculum.updateSectionTitle(1, "S1 Updated");

        assertThat(curriculum.getSections()).extracting(Section::getTitle)
                .containsExactly("S0 Updated", "S1 Updated");
    }

    @Test
    void updatedLessonTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section section0 = curriculum.addSection("S0");
        Section section1 = curriculum.addSection("S1");
        Lesson lesson0_0 = curriculum.addLesson(0, "L0_0");
        Lesson lesson0_1 = curriculum.addLesson(0, "L0_1");
        Lesson lesson1 = curriculum.addLesson(1, "L1");

        curriculum.updateLessonTitle(0, 0, "L0_0 Updated");

        assertThat(lesson0_0.getTitle()).isEqualTo("L0_0 Updated");

        curriculum.updateLessonTitle(1, 0, "L1 Updated");

        assertThat(lesson1.getTitle()).isEqualTo("L1 Updated");

        List<Lesson> lessons = curriculum.getSections()
                .stream()
                .flatMap(section -> section.getLessons().stream())
                .toList();

        assertThat(lessons).extracting(Lesson::getTitle)
                .containsExactly("L0_0 Updated", "L0_1", "L1 Updated");
    }

}