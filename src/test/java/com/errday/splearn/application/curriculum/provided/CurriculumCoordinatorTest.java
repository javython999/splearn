package com.errday.splearn.application.curriculum.provided;

import com.errday.splearn.application.curriculum.required.CurriculumRepository;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.InvalidCurriculumException;
import com.errday.splearn.domain.curriculum.SectionContent;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.errday.splearn.domain.curriculum.LessonContent.lesson;
import static com.errday.splearn.domain.curriculum.SectionContent.section;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumCoordinatorTest extends BaseApplicationServiceTest {
    final CurriculumCoordinator curriculumCoordinator;
    final CurriculumRepository curriculumRepository;

    @Test
    void create() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();

        assertThat(curriculum.getId()).isNotNull();
    }

    @Test
    void addSection() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();

        assertThat(SectionContent.from(curriculum)).isEmpty();

        Curriculum updated = curriculumCoordinator.addSection(curriculum.getId(), "S0");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0")
        );
    }

    @Test
    void addSectionWithSectionIndex() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0"),
                section("S1")
        );

        Curriculum updated =  curriculumCoordinator.addSection(curriculum.getId(), 1, "S2");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0"),
                section("S2"),
                section("S1")
        );
    }

    @Test
    void addLesson() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");

        Curriculum updated = curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L0")),
                section("S1")
        );

        updated = curriculumCoordinator.addLesson(curriculum.getId(), 1, "L1");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L0")),
                section("S1", lesson("L1"))
        );

        updated = curriculumCoordinator.addLesson(curriculum.getId(), 0, "L2");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L0"), lesson("L2")),
                section("S1", lesson("L1"))
        );
    }

    @Test
    void updateSectionTitle() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0"),
                section("S1")
        );

        Curriculum updated = curriculumCoordinator.updateSectionTitle(curriculum.getId(), 0, "Section0");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("Section0"),
                section("S1")
        );
    }

    @Test
    void updateLessonTitle() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L1");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");
        curriculumCoordinator.addLesson(curriculum.getId(), 1, "L2");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"))
        );

        Curriculum updated = curriculumCoordinator.updateLessonTitle(curriculum.getId(), 0, 1, "Lesson1");

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L0"), lesson("Lesson1")),
                section("S1", lesson("L2"))
        );
    }

    @Test
    void removeLesson() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L1");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");
        curriculumCoordinator.addLesson(curriculum.getId(), 1, "L2");
        curriculumCoordinator.addSection(curriculum.getId(), "S2");
        curriculumCoordinator.addLesson(curriculum.getId(), 2, "L3");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );

        Curriculum updated = curriculumCoordinator.removeLesson(curriculum.getId(), 0, 1);

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L0")),
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );

        assertThatThrownBy(() -> curriculumCoordinator.removeLesson(curriculum.getId(), 0, 1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void removeSection() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L1");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");
        curriculumCoordinator.addLesson(curriculum.getId(), 1, "L2");
        curriculumCoordinator.addSection(curriculum.getId(), "S2");
        curriculumCoordinator.addLesson(curriculum.getId(), 2, "L3");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );

        Curriculum updated = curriculumCoordinator.removeSection(curriculum.getId(), 0);

        // 첫번째 섹션이 삭제되면 강의들은 그 다음 섹션의 앞에 추가된다.
        assertThat(SectionContent.from(updated)).containsExactly(
                section("S1", lesson("L0"), lesson("L1"), lesson("L2")),
                section("S2", lesson("L3"))
        );

        updated = curriculumCoordinator.removeSection(curriculum.getId(), 1);

        // 맨 뒤의 강의가 삭제되면 강의들이 그 앞 섹션에 뒤에 추가된다.
        assertThat(SectionContent.from(updated)).containsExactly(
                section("S1", lesson("L0"), lesson("L1"), lesson("L2"), lesson("L3"))
        );

        assertThatThrownBy(() -> curriculumCoordinator.removeSection(curriculum.getId(), 1))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void moveLesson() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L1");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");
        curriculumCoordinator.addLesson(curriculum.getId(), 1, "L2");
        curriculumCoordinator.addSection(curriculum.getId(), "S2");
        curriculumCoordinator.addLesson(curriculum.getId(), 2, "L3");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );

        Curriculum updated = curriculumCoordinator.moveLesson(curriculum.getId(), 0, 0, 1, 0);

        assertThat(SectionContent.from(updated)).containsExactly(
                section("S0", lesson("L1")),
                section("S1", lesson("L0"), lesson("L2")),
                section("S2", lesson("L3"))
        );
    }

    @Test
    void validate() {
        Curriculum curriculum = curriculumRepository.findByCourseId(prepareCourse().getId()).orElseThrow();
        curriculumCoordinator.addSection(curriculum.getId(), "S0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
        curriculumCoordinator.addLesson(curriculum.getId(), 0, "L1");
        curriculumCoordinator.addSection(curriculum.getId(), "S1");
        curriculumCoordinator.addLesson(curriculum.getId(), 1, "L2");
        curriculumCoordinator.addSection(curriculum.getId(), "S2");
        curriculumCoordinator.addLesson(curriculum.getId(), 2, "L3");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2")),
                section("S2", lesson("L3"))
        );

        curriculum.validate();

        Curriculum updated = curriculumCoordinator.addSection(curriculum.getId(), "emptySection");

        assertThatThrownBy(updated::validate)
                .isInstanceOf(InvalidCurriculumException.class);

    }


}