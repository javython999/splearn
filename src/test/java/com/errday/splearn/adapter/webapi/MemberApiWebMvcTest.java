package com.errday.splearn.adapter.webapi;

import com.errday.splearn.application.member.provided.MemberRegister;
import com.errday.splearn.domain.member.Member;
import com.errday.splearn.domain.member.MemberFixture;
import com.errday.splearn.domain.member.MemberResisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {

    final MockMvcTester  mvcTester;
    final  ObjectMapper objectMapper;

    @MockitoBean
    private MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any()))
                .thenReturn(member);

        MemberResisterRequest request = MemberFixture.createMemberRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post()
                .uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() throws JsonProcessingException {
        MemberResisterRequest request = MemberFixture.createMemberRequest("invalidEmail");
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post()
                .uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson))
                .hasStatus(HttpStatus.BAD_REQUEST);

    }
}