package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.AbstractEntity;
import com.errday.splearn.domain.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.*;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"course", "sections"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum extends AbstractEntity {
    @OneToOne
    private Course course;

    @OneToMany
    @Getter(AccessLevel.NONE)
    private List<Section> sections = new ArrayList<>();

    public Curriculum(Course course) {
        this.course = Objects.requireNonNull(course);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Section addSection(String title) {
        Section section = new Section(this, title);

        sections.add(section);

        return section;
    }

    public Section addSection(int sectionIndex, String title) {
        Objects.checkIndex(sectionIndex, sections.size() + 1);

        Section section = new Section(this, title);

        sections.add(sectionIndex, section);

        return section;
    }

    public Lesson addLesson(int sectionIndex, String title) {
        return sections.get(sectionIndex).addLesson(title);
    }

    public void updateSectionTitle(int sectionIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateTitle(title);
    }

    public void updateLessonTitle(int sectionIndex, int lessonIndex, String title) {
        Section section = sections.get(sectionIndex);
        section.updateLessonTitle(lessonIndex, title);
    }

    public Lesson removeLesson(int sectionIndex, int lessonIndex) {
        return sections.get(sectionIndex).removeLesson(lessonIndex);
    }

    public List<Lesson> allLessons() {
        return getSections().stream()
                .flatMap(section -> section.getLessons().stream())
                .toList();
    }

    public Section removeSection(int sectionIndex) {
        state(this.sections.size() > 1, "마지막 남은 섹션은 삭제할 수 없습니다.");

        Section removed = sections.remove(sectionIndex);

        if (sectionIndex == 0) {
            Section next = sections.get(0);
            removed.moveAllLessonsTo(next, 0);
        }
        else {
            Section previous = sections.get(sectionIndex - 1);
            removed.moveAllLessonsTo(previous, previous.getLessons().size());
        }

        return removed;
    }

    public void moveLesson(int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex) {
        Section from = this.sections.get(fromSectionIndex);
        Section to = this.sections.get(toSectionIndex);

        to.addLesson(toLessonIndex, from.removeLesson(fromLessonIndex));
    }

    public void validate() {
        if (this.sections.isEmpty()) {
            throw new InvalidCurriculumException("최소한 하나의 섹션이 필요합니다.");
        }

        this.sections.forEach(section -> {
            if (section.getLessons().isEmpty()) {
                throw new InvalidCurriculumException("수업이 없는 섹션은 허용되지 않습니다.");
            }
        });

    }

    public Optional<Lesson> firstLesson() {
        return allLessons().stream().findFirst();
    }

    public Optional<Lesson> nextLesson(Lesson lesson) {
        List<Lesson> lessons = allLessons();

        int index = lessons.indexOf(lesson);

        Assert.isTrue(index >= 0, "커리큘럼에 포함된 수업이 아닙니다.");

        if (index + 1 >= lessons.size()) {
            return Optional.empty();
        }

        return Optional.of(lessons.get(index + 1));
    }

    public Optional<Lesson> nextLesson(Long lessonId) {
        Lesson lesson = allLessons().stream()
                .filter(candidate -> lessonId.equals(candidate.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("수업을 찾을 수 없습니다. 수업 ID: " + lessonId));

        return nextLesson(lesson);
    }
}
