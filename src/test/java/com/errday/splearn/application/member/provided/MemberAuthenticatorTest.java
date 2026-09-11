package com.errday.splearn.application.member.provided;

import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class MemberAuthenticatorTest {
    final MemberAuthenticator memberAuthenticator;
    final MemberRegister memberRegister;

    @Test
    void login() {
        var registerRequest = MemberFixture.createMemberRegisterRequest();
        var member = memberRegister.register(registerRequest);
        member.activate();

        var loggedInMember = memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password()));

        assertThat(loggedInMember).isEqualTo(member);
    }

    @Test
    void loginFailedNotActive() {
        var registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest);

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailedEmailNotExist() {
        var registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest).isActive();

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest("not_exist_email@test.com", registerRequest.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailedWrongPassword() {
        var registerRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registerRequest).isActive();

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), "wrong_password")))
                .isInstanceOf(LoginFailedException.class);
    }
}