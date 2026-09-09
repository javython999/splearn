package com.errday.splearn.domain.instructor;

import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InstructorTest {

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void applyFailedMemberNotActive() {
        Member member = MemberFixture.createMember();

        assertThatThrownBy(() -> Instructor.apply(member))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void approve() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.approve();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFailed() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(instructor::approve)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.reject();

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFailed() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.reject();

        assertThatThrownBy(instructor::reject)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThatThrownBy(instructor::ensureActive)
            .isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }
}