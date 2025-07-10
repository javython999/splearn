package com.errday.splearn.application.member.provided;

import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberResisterRequest;
import jakarta.validation.Valid;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
    Member register(@Valid MemberResisterRequest resisterRequest);

    Member activate(Long memberId);
}
