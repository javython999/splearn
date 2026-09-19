package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"curriculum", "lessons"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends AbstractEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curriculum curriculum;

    @Column(length = 200)
    private String title;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter(AccessLevel.NONE)
    private List<Lesson> lessons = new ArrayList<>();

    List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    Section(Curriculum curriculum, String title) {
        this.curriculum = curriculum;
        this.title = Objects.requireNonNull(title);
    }

    Lesson addLesson(String title) {
        Lesson lesson = new Lesson(this, title);

        this.lessons.add(lesson);

        return lesson;
    }

    void updateTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    void updateLessonTitle(int lessonIndex, String title) {
        this.lessons.get(lessonIndex).updateTitle(title);
    }

    Lesson removeLesson(int lessonIndex) {
        return this.lessons.remove(lessonIndex);
    }

    void moveAllLessonsTo(Section target, int insertIndex) {
        while (!lessons.isEmpty()) {
            target.addLesson(insertIndex++, lessons.getFirst());
            this.lessons.removeFirst();
        }
    }

    void addLesson(int insertIndex, Lesson lesson) {
        lesson.moveTo(this);
        this.lessons.add(insertIndex, lesson);
    }
}
