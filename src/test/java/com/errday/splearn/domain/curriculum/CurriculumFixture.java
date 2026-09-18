package com.errday.splearn.domain.curriculum;

import com.errday.splearn.domain.course.CourseFixture;

public class CurriculumFixture {
    public static Curriculum createCurriculum() {
        return new Curriculum(CourseFixture.createCourse());
    }
}
