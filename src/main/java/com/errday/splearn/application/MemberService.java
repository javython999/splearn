package com.errday.splearn.application;

import com.errday.splearn.application.provided.MemberRegister;
import com.errday.splearn.application.required.EmailSender;
import com.errday.splearn.application.required.MemberRepository;
import com.errday.splearn.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberResisterRequest resisterRequest) {
        checkDuplicateEmail(resisterRequest);

        Member member = Member.register(resisterRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    private void checkDuplicateEmail(MemberResisterRequest resisterRequest) {
        if (memberRepository.findByEmail(new Email(resisterRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다. : " + resisterRequest.email());
        }
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }
}
