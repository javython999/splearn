package com.errday.splearn.adapter.webapi;

import com.errday.splearn.application.member.provided.MemberRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.errday.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.member.Member;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
