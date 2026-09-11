package com.errday.splearn.application.member.required;

import com.errday.splearn.application.member.provided.MemberRegisterRequest;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.errday.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static com.errday.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class MemberRepositoryTest {
    final MemberRepository memberRepository;
    final EntityManager entityManager;

    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        var found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }


    @Test
    void duplicateEmailFail() {
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest();

        Member member = Member.register(memberRegisterRequest.toInfo(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(memberRegisterRequest.toInfo(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
        memberRepository.save(member2);
    }
}