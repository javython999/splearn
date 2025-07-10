package com.errday.splearn.application.member;

import com.errday.splearn.application.member.provided.MemberFinder;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found. id: " + memberId));
    }
}
