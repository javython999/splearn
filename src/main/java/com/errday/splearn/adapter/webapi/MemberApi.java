package com.errday.splearn.adapter.webapi;

import com.errday.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberResisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRegister memberRegister;

    // register api -> /members POST
    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberResisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }



}
