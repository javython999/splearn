package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.AbstractEntity;
import com.errday.splearn.domain.course.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum extends AbstractEntity {
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Curriculum(Course course) {
        this.course = Objects.requireNonNull(course);
    }

    Section addSection(String title) {
        Section section = new Section(this, title);

        sections.add(section);

        return section;
    }

    Section addSection(int sectionIndex, String title) {
        Objects.checkIndex(sectionIndex, sections.size() + 1);

        Section section = new Section(this, title);

        sections.add(sectionIndex, section);

        return section;
    }

    Lesson addLesson(int sectionIndex, String title) {
        return sections.get(sectionIndex).addLesson(title);
    }

    Section updateSectionTitle(int sectionIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateTitle(title);
        return section;
    }

    void updateLessonTitle(int sectionIndex, int lessonIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateLessonTitle(lessonIndex, title);
    }
}
