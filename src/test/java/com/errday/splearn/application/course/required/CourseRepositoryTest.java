package com.errday.splearn.application.course.required;

import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.course.CourseFixture;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest {
    final CourseRepository courseRepository;
    final EntityManager entityManager;
    final MemberRepository memberRepository;
    final InstructorRepository instructorRepository;

    Member member;
    Instructor instructor;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.createActiveMember());
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
    }

    @Test
    void save() {
        Member member = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));

        Course course = CourseFixture.createCourse(instructor);
        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void saveAndFindId() {
        Member member = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));

        Course course = CourseFixture.createCourse(instructor);
        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        Course found = courseRepository.findById(course.getId()).orElseThrow();

        assertThat(course).isEqualTo(found);
    }

    @Test
    void findByTitleContaining() {
        List<Long> ids = Stream.of(
                        CourseFixture.createCourse(instructor, "Hello Spring"),
                        CourseFixture.createCourse(instructor, "Clean Spring2"),
                        CourseFixture.createCourse(instructor, "Clean Code"))
                .map(course -> courseRepository.save(course).getId())
                .toList();

        assertThat(courseRepository.findByTitleContaining("Spring").stream().map(Course::getId))
                .isEqualTo(List.of(ids.get(0), ids.get(1)));

        assertThat(courseRepository.findByTitleContaining("Clean").stream().map(Course::getId))
                .isEqualTo(List.of(ids.get(1), ids.get(2)));

        assertThat(courseRepository.findByTitleContaining("Code").stream().map(Course::getId))
                .isEqualTo(List.of(ids.get(2)));

        assertThat(courseRepository.findByTitleContaining("JPA").stream().map(Course::getId))
                .isEqualTo(Collections.emptyList());
    }

    @Test
    void findByInstructor() {
        var member2 = memberRepository.save(MemberFixture.createActiveMember());
        var instructor2 = instructorRepository.save(InstructorFixture.createActiveInstructor(member2));

        var course = courseRepository.save(CourseFixture.createCourse(instructor, "title"));
        var course2 = courseRepository.save(CourseFixture.createCourse(instructor2, "title2"));

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());
        assertThat(courses).singleElement().isEqualTo(course);

        List<Course> courses2 = courseRepository.findByInstructorId(instructor2.getId());
        assertThat(courses2).singleElement().isEqualTo(course2);

        List<Course> courses2_1 = courseRepository.findByInstructor(instructor2);
        assertThat(courses2_1).singleElement().isEqualTo(course2);
    }

    @Test
    void uniqueTitleAndInstructor() {
        courseRepository.save(CourseFixture.createCourse(instructor, "title"));

        assertThatThrownBy(() -> courseRepository.save(CourseFixture.createCourse(instructor, "title")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}