package com.errday.splearn.application.provided;

import com.errday.splearn.domain.Member;
import com.errday.splearn.domain.MemberResisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
    Member register(MemberResisterRequest resisterRequest);
}
