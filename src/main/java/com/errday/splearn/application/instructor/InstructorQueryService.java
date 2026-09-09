package com.errday.splearn.application.instructor;

import com.errday.splearn.application.instructor.provided.InstructorFinder;
import com.errday.splearn.application.instructor.required.InstructorRepository;
import com.errday.splearn.domain.instructor.Instructor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
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
