package com.errday.splearn.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberResisterRequest(
        @Email String email,
        @Size(min = 5, max = 20) String nickname,
        @Size(min = 8, max = 100) String password) {
}
