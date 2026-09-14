package com.errday.splearn.application.instructor.provided;

import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest extends BaseApplicationServiceTest {
    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;


    @Test
    void findByMember() {
        prepareMember();

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(instructor).isEqualTo(found);
        assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
    }

}