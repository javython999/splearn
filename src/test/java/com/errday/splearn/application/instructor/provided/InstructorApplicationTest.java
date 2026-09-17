package com.errday.splearn.application.instructor.provided;

import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import com.errday.splearn.domain.instructor.InstructorStatus;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorApplicationTest extends BaseApplicationServiceTest {
    final InstructorApplication instructorApplication;
    final InstructorRepository instructorRepository;


    @Test
    void apply() {
        prepareActiveMember();

        Instructor instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        assertThat(instructor.getId()).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        instructorRepository.findById(instructor.getId()).orElseThrow();
    }

    @Test
    void duplicateApply() {
        prepareActiveMember();

        instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThatThrownBy(
                () -> instructorApplication.apply(InstructorFixture.createApplyRequest(member)))
                .isInstanceOf(DuplicateInstructorApplicationException.class);
    }

    @Test
    void approve() {
        Instructor instructor = instructorApplication.approve(preparePendingInstroctor().getId());

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void reject() {
        Instructor instructor = instructorApplication.reject(preparePendingInstroctor().getId());

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    private Instructor preparePendingInstroctor() {
        prepareActiveMember();
        return instructorApplication.apply(InstructorFixture.createApplyRequest(member));
    }
}