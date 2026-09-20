package com.errday.splearn.application.curriculum.required;

import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.Lesson;
import com.errday.splearn.domain.curriculum.Section;
import com.errday.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.errday.splearn.domain.curriculum.LessonContent.lesson;
import static com.errday.splearn.domain.curriculum.SectionContent.from;
import static com.errday.splearn.domain.curriculum.SectionContent.section;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
class CurriculumRepositoryTest extends BaseRepositoryTest {
    final CurriculumRepository curriculumRepository;
    final SectionRepository sectionRepository;
    final LessonRepository lessonRepository;

    @Test
    void saveAndFindById() {
        Long curriculumId = saveCurriculum();

        Statistics statistics = prepareStatistics();

        Curriculum found = curriculumRepository.findById(curriculumId).orElseThrow();

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);

        assertThat(found.getId()).isEqualTo(curriculumId);

        assertThat(from(found)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"))
        );

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(4);
    }

    @Test
    void removeLesson() {
        Long curriculumId = saveCurriculum();
        Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        Lesson lesson = curriculum.removeLesson(0, 0);
        lessonRepository.delete(lesson);

        curriculumRepository.save(curriculum);

        entityManager.flush();
        entityManager.clear();

        curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        assertThat(from(curriculum)).containsExactly(
                section("S0", lesson("L1")),
                section("S1", lesson("L2"))
        );
    }

    @Test
    void removeSection() {
        Long curriculumId = saveCurriculum();
        Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        Section section = curriculum.removeSection(0);
        sectionRepository.delete(section);

        entityManager.flush();
        entityManager.clear();

        curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        assertThat(from(curriculum)).containsExactly(
                section("S1", lesson("L0"), lesson("L1"), lesson("L2"))
        );
    }

    @Test
    void moveLesson() {
        Long curriculumId = saveCurriculum();
        Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        curriculum.moveLesson(0, 0, 1, 1);

        entityManager.flush();
        entityManager.clear();

        curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        assertThat(from(curriculum)).containsExactly(
                section("S0", lesson("L1")),
                section("S1", lesson("L2"), lesson("L0"))
        );
    }

    @Test
    void saveAndFindWithSectionsById() {
        Long curriculumId = saveCurriculum();

        Statistics statistics = prepareStatistics();

        Curriculum found = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);

        assertThat(found.getId()).isEqualTo(curriculumId);

        assertThat(from(found)).containsExactly(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"))
        );

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    private Long saveCurriculum() {
        Curriculum curriculum = new Curriculum(prepareCourse());
        curriculum = curriculumRepository.save(curriculum);

        curriculum.addSection("S0");
        curriculum.addLesson(0, "L0");
        curriculum.addLesson(0, "L1");
        curriculum.addSection("S1");
        curriculum.addLesson(1, "L2");

        entityManager.flush();
        entityManager.clear();
        return curriculum.getId();
    }


}