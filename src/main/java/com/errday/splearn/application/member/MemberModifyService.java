package com.errday.splearn.application.member;

import com.errday.splearn.application.member.provided.MemberFinder;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.application.member.required.EmailSender;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.*;
import com.errday.splearn.domain.member.*;
import com.errday.splearn.domain.shared.Email;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class MemberModifyService implements MemberRegister{

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(@Valid MemberResisterRequest resisterRequest) {
        checkDuplicateEmail(resisterRequest);

        Member member = Member.register(resisterRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest updateRequest) {
        Member member = memberFinder.find(memberId);

        checkDuplicateProfile(member, updateRequest.profileAddress());

        member.updateInfo(updateRequest);

        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if (profileAddress.isEmpty()) {
            return;
        }

        Profile currentProfile = member.getDetail().getProfile();

        if (currentProfile != null && currentProfile.address().equals(profileAddress)) {
            return;
        }

        if (memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미 존재하는 프로필 주소입니다. " +  profileAddress);
        }
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
