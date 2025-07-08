package com.errday.splearn.domain;

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
}
