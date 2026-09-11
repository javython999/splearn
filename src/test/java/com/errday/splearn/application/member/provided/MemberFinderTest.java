package com.errday.splearn.application.member.provided;

import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager) {
    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member find = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(find.getId());
    }

    @Test
    void findFail() {
        Assertions.assertThatThrownBy(() -> memberFinder.find(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }


}