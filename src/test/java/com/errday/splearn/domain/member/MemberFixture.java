package com.errday.splearn.domain.member;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static MemberResisterRequest createMemberRequest(String email) {
        return new MemberResisterRequest(email, "nickname", "supersecret");
    }

    public static MemberResisterRequest createMemberRequest() {
        return createMemberRequest("test@test.com");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static Member createMember() {
        return  Member.register(createMemberRequest(), createPasswordEncoder());
    }

    public static Member createMember(Long id) {
        Member member = Member.register(createMemberRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createMember(String email) {
        return Member.register(createMemberRequest(email), createPasswordEncoder());
    }
}
