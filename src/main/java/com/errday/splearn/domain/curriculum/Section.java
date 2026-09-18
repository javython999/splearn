package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.AbstractEntity;
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
public class Section extends AbstractEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curriculum curriculum;

    @Column(length = 200)
    private String title;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    Section(Curriculum curriculum, String title) {
        this.curriculum = curriculum;
        this.title = Objects.requireNonNull(title);
    }

    Lesson addLesson(String title) {
        Lesson lesson = new Lesson(this, title);

        lessons.add(lesson);

        return lesson;
    }

    void updateTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    void updateLessonTitle(int lessonIndex, String title) {
        this.lessons.get(lessonIndex).updateTitle(title);
    }
}
