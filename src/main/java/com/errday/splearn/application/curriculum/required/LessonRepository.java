package com.errday.splearn.application.curriculum.required;

import com.errday.splearn.domain.curriculum.Lesson;
import org.springframework.data.repository.Repository;

public interface LessonRepository extends Repository<Lesson, Long> {
    void delete(Lesson lesson);
}
