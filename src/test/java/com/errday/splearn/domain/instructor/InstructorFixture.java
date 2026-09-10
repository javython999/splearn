package com.errday.splearn.domain.instructor;

import com.errday.splearn.application.instructor.provided.InstructorApplyRequest;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import jakarta.validation.Valid;

public class InstructorFixture {


    public static Instructor createInstructor(Member member) {
        return Instructor.apply(member);
    }

    public static Instructor createInstructor() {
        return createInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor() {
        Instructor instructor = createInstructor();
        instructor.approve();
        return instructor;
    }

    public static @Valid InstructorApplyRequest createApplyRequest(Member member) {
        return new InstructorApplyRequest(member.getId());
    }
}
