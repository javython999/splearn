package com.errday.splearn.adapter.webapi;

import com.errday.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.application.member.provided.MemberRegisterRequest;
import com.errday.splearn.application.member.required.MemberRepository;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.domain.member.MemberStatus;
import com.errday.splearn.support.stereotype.WebApiAdapterTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.io.UnsupportedEncodingException;

import static com.errday.splearn.AssertThatUtils.equalsTo;
import static com.errday.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebApiAdapterTest
@RequiredArgsConstructor
public class MemberApiTest {
    final MockMvcTester  mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post()
                .uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request));


        MemberRegisterResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @Test
    void duplicateFail() throws JsonProcessingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        memberRegister.register(request);

        String requestJson = objectMapper.writeValueAsString(request);


        MvcTestResult result = mvcTester.post()
                .uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }

}
