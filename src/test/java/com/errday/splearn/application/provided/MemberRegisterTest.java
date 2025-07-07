package com.errday.splearn.application.provided;

import com.errday.splearn.SplearnTestConfiguration;
import com.errday.splearn.domain.DuplicateEmailException;
import com.errday.splearn.domain.Member;
import com.errday.splearn.domain.MemberFixture;
import com.errday.splearn.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

}
