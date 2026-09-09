package com.errday.splearn.application.instructor.provided;

import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class InstructorFinderTest {
    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;
    final MemberRegister memberRegister;
    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    void find() {
    }

    @Test
    void findByMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRequest());
        member = memberRegister.activate(member.getId());

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(instructor).isEqualTo(found);
        assertThat(instructorRepository.findByMemberId(Long.MAX_VALUE).isPresent()).isFalse();
    }

    @Test
    void testFindByMember() {
    }
}