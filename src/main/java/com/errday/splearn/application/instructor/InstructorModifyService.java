package com.errday.splearn.application.instructor;

import com.errday.splearn.application.instructor.provided.DuplicateInstructorApplicationException;
import com.errday.splearn.application.instructor.provided.InstructorApplication;
import com.errday.splearn.application.instructor.provided.InstructorApplyRequest;
import com.errday.splearn.application.instructor.provided.InstructorFinder;
import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.application.member.provided.MemberFinder;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.domain.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {

    private final InstructorRepository instructorRepository;
    private final InstructorFinder instructorFinder;
    private final MemberFinder memberFinder;

    @Override
    public Instructor apply(InstructorApplyRequest request) {
        Member member = memberFinder.find(request.memberId());

        checkDuplicateApplication(member);

        Instructor instructor = Instructor.apply(member);

        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor approve(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);
        instructor.approve();
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor reject(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);
        instructor.reject();
        return instructorRepository.save(instructor);
    }

    private void checkDuplicateApplication(Member member) {
        if (instructorRepository.findByMemberId(member.getId()).isPresent()) {
            throw new DuplicateInstructorApplicationException("회원은 중복해서 강사 신청을 할 수 없습니다.");
        }
    }
}

