package com.errday.splearn.application.required;

import com.errday.splearn.domain.Member;
import org.springframework.data.repository.Repository;

/**
 * 회원 정보를 저장하거나 조회한다.
 */
public interface MemberRepository extends Repository<Member,String> {
    Member save(Member member);
}
