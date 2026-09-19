package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"section"})
@NoArgsConstructor
public class Lesson extends AbstractEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Section section;

    @Column(length = 200)
    private String title;

    public Lesson(Section section, String title) {
        this.section = section;
        this.title = title;
    }

    void updateTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public void moveTo(Section section) {
        this.section = section;
    }
}
