package com.errday.splearn.support.test;

import com.errday.splearn.application.instructor.provided.InstructorApplication;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.instructor.InstructorFixture;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    protected Member member;
    protected Instructor instructor;

    @NonNull
    protected Instructor prepareInstructor() {
        member = prepareMember();

        instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        instructor.approve();

        return instructor;
    }

    protected @NonNull Member prepareMember() {
        member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        member.activate();

        return member;
    }


}
