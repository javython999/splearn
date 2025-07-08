package com.errday.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.errday.splearn.domain.MemberFixture.createMemberRequest;
import static com.errday.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();

        member = Member.register(createMemberRequest(), passwordEncoder);
    }



    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    /*
    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> Member.create(null, "user", "secret", passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }
    */

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("supersecret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("nickname");

        member.changeNickname("junit5");

        assertThat(member.getNickname()).isEqualTo("junit5");
    }

    @Test
    void changePassword() {
        String newPassword = "newPassword";
        member.changePassword(newPassword, passwordEncoder);

        assertThat(member.verifyPassword(newPassword, passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> {
            Member.register(createMemberRequest("invalid Email"), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRequest(), passwordEncoder);
    }
}