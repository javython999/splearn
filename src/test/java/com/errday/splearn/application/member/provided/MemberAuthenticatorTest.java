package com.errday.splearn.application.member.provided;

import com.errday.splearn.SplearnTestConfiguration;
import com.errday.splearn.domain.member.MemberFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
class MemberAuthenticatorTest {

    @Autowired
    private MemberAuthenticator memberAuthenticator;

    @Autowired
    private MemberRegister memberRegister;

    @Test
    void login() {
        var registerRequest = MemberFixture.createMemberRequest();
        var member = memberRegister.register(registerRequest);
        member.activate();

        var loggedInMember = memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password()));

        assertThat(loggedInMember).isEqualTo(member);
    }

    @Test
    void loginFailedNotActive() {
        var registerRequest = MemberFixture.createMemberRequest();
        memberRegister.register(registerRequest);

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), registerRequest.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailedEmailNotExist() {
        var registerRequest = MemberFixture.createMemberRequest();
        memberRegister.register(registerRequest).isActive();

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest("not_exist_email@test.com", registerRequest.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFailedWrongPassword() {
        var registerRequest = MemberFixture.createMemberRequest();
        memberRegister.register(registerRequest).isActive();

        assertThatThrownBy(() ->
                memberAuthenticator.login(new MemberLoginRequest(registerRequest.email(), "wrong_password")))
                .isInstanceOf(LoginFailedException.class);
    }
}