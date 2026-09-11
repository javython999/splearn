package com.errday.splearn.adapter.webapi;

import com.errday.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.application.member.provided.MemberRegisterRequest;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.support.stereotype.WebApiAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebApiAdapter
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
