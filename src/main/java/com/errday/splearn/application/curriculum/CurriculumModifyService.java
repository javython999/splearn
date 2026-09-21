package com.errday.splearn.application.curriculum;

import com.errday.splearn.application.course.provided.CourseFinder;
import com.errday.splearn.application.curriculum.provided.CurriculumCoordinator;
import com.errday.splearn.application.curriculum.provided.CurriculumFinder;
import com.errday.splearn.application.curriculum.required.CurriculumRepository;
import com.errday.splearn.application.curriculum.required.LessonRepository;
import com.errday.splearn.application.curriculum.required.SectionRepository;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.domain.curriculum.InvalidCurriculumException;
import com.errday.splearn.domain.curriculum.Lesson;
import com.errday.splearn.domain.curriculum.Section;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import static java.util.Objects.requireNonNull;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumModifyService implements CurriculumCoordinator {
    private final CurriculumRepository curriculumRepository;
    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;
    private final CurriculumFinder curriculumFinder;
    private final CourseFinder courseFinder;

    @Override
    public Long createCurriculum(Course course) {
        Curriculum curriculum = new Curriculum(course);

        return curriculumRepository.save(curriculum).getId();
    }

    @Override
    public Curriculum addSection(Long curriculumId, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.addSection(title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum addSection(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.addSection(sectionIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum addLesson(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.addLesson(sectionIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.updateSectionTitle(sectionIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.updateLessonTitle(sectionIndex, lessonIndex, title);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        Lesson lesson = curriculum.removeLesson(sectionIndex, lessonIndex);
        lessonRepository.delete(lesson);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum removeSection(Long curriculumId, int sectionIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        Section section = curriculum.removeSection(sectionIndex);
        sectionRepository.delete(section);

        return curriculumRepository.save(curriculum);
    }

    @Override
    public Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex) {
        Curriculum curriculum = curriculumFinder.find(curriculumId);

        curriculum.moveLesson(fromSectionIndex, fromLessonIndex, toSectionIndex, toLessonIndex);

        return curriculumRepository.save(curriculum);
    }


    @Override
    public void validate(Long courseId) throws InvalidCurriculumException {
        Curriculum curriculum = curriculumFinder.findByCourseId(courseId);

        curriculum.validate();
    }
}
