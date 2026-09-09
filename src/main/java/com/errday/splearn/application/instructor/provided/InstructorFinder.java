package com.errday.splearn.application.instructor.provided;

import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.member.Member;

import java.util.Optional;

/**
 * 강사 조회
 */
public interface InstructorFinder {
    Instructor find(Long instructorId);

    Optional<Instructor> findByMember(Long memberId);

    default Optional<Instructor> findByMember(Member member) {
        return findByMember(member.getId());
    }
}
