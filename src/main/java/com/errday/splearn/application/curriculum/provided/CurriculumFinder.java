package com.errday.splearn.application.curriculum.provided;

import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.Lesson;

import java.util.Optional;

public interface CurriculumFinder {
    Curriculum find(Long curriculumId);

    Curriculum findWithSections(Long curriculumId);

    Curriculum findByCourseId(Long courseId);

    Optional<Lesson> firstLesson(Long curriculumId);

    Optional<Lesson> nextLesson(Long curriculumId, Long lessonId);
}
