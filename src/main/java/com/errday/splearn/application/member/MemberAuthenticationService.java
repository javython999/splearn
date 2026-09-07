package com.errday.splearn.application.member;

import com.errday.splearn.application.member.provided.LoginFailedException;
import com.errday.splearn.application.member.provided.MemberAuthenticator;
import com.errday.splearn.application.member.provided.MemberLoginRequest;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.PasswordEncoder;
import com.errday.splearn.domain.shared.Email;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberAuthenticationService implements MemberAuthenticator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest request) throws LoginFailedException {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(LoginFailedException::new);

        if (!member.isActive()) {
            throw new LoginFailedException();
        }

        if (!member.verifyPassword(request.password(), passwordEncoder)) {
            throw new LoginFailedException();
        }

        return member;
    }
}

