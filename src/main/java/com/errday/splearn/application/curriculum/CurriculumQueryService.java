package com.errday.splearn.application.curriculum;

import com.errday.splearn.application.curriculum.provided.CurriculumFinder;
import com.errday.splearn.application.curriculum.required.CurriculumRepository;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.Lesson;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumQueryService implements CurriculumFinder {
    private final CurriculumRepository curriculumRepository;

    @Override
    public Curriculum find(Long curriculumId) {
        return curriculumRepository.findById(curriculumId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID = " + curriculumId)
        );
    }

    @Override
    public Curriculum findWithSections(Long curriculumId) {
        return curriculumRepository.findWithSectionsById(curriculumId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID = " + curriculumId)
        );
    }

    @Override
    public Curriculum findByCourseId(Long courseId) {
        return curriculumRepository.findByCourseId(courseId).orElseThrow(
                () -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다 courseId = " + courseId)
        );
    }

    @Override
    public Optional<Lesson> firstLesson(Long curriculumId) {
        return findWithSections(curriculumId).firstLesson();
    }

    @Override
    public Optional<Lesson> nextLesson(Long curriculumId, Long lessonId) {
        return findWithSections(curriculumId).nextLesson(lessonId);
    }
}
