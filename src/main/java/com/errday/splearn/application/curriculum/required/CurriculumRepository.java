package com.errday.splearn.application.curriculum.required;

import com.errday.splearn.domain.curriculum.Curriculum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CurriculumRepository extends Repository<Curriculum, Long> {
    Curriculum save(Curriculum curriculum);

    Optional<Curriculum> findById(Long curriculumId);

    @EntityGraph(attributePaths = {"sections", "sections.lessons"})
    Optional<Curriculum> findWithSectionsById(Long curriculumId);

    Optional<Curriculum> findByCourseId(Long courseId);
}
