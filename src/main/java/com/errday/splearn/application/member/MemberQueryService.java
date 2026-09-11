package com.errday.splearn.application.member;

import com.errday.splearn.application.member.provided.MemberFinder;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
    }
}
