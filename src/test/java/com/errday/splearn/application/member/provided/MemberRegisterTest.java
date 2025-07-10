package com.errday.splearn.application.member.provided;

import com.errday.splearn.SplearnTestConfiguration;
import com.errday.splearn.domain.*;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.domain.member.MemberResisterRequest;
import com.errday.splearn.domain.member.MemberStatus;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager){

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);

    }

    @Test
    void memberRegisterRequestFail() {
        checkValidation(new MemberResisterRequest("test@test.com", "user", "longSecret"));
        checkValidation(new MemberResisterRequest("test@test.com", "intellij___________________", "longSecret"));
        checkValidation(new MemberResisterRequest("testtest.com", "intellij", "longSecret"));
    }

    private void checkValidation(MemberResisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
