package com.errday.splearn.application.instructor;

import com.errday.splearn.application.instructor.provided.InstructorFinder;
import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.domain.instructor.Instructor;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class InstructorQueryService implements InstructorFinder {

    private final InstructorRepository instructorRepository;


    @Override
    public Instructor find(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found. Id: " + instructorId));
    }

    @Override
    public Optional<Instructor> findByMember(Long memberId) {
        return instructorRepository.findByMemberId(memberId);
    }

}
