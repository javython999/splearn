package com.errday.splearn.application.member.provided;

import com.errday.splearn.domain.member.Member;
import jakarta.validation.Valid;

/**
 * 회원 인증
 * - ACTIVE 상태인 회원만 로그인할 수 있다.
 */
public interface MemberAuthenticator {
    Member login(@Valid MemberLoginRequest request) throws LoginFailedException;
}
