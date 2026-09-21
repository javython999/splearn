package com.errday.splearn.application.curriculum.provided;

import com.errday.splearn.application.course.required.CurriculumCreator;
import com.errday.splearn.application.course.required.CurriculumValidator;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.InvalidCurriculumException;

public interface CurriculumCoordinator extends CurriculumCreator, CurriculumValidator {
    Curriculum addSection(Long curriculumId, String title);

    Curriculum addSection(Long curriculumId, int sectionIndex, String title);

    Curriculum addLesson(Long curriculumId, int sectionIndex, String title);

    Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title);

    Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title);

    Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex);

    Curriculum removeSection(Long curriculumId, int sectionIndex);

    Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex);
}
