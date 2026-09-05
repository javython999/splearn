package com.errday.splearn.application.member.provided;

import com.errday.splearn.domain.member.MemberInfoUpdateInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRequest(
        @Size(min = 5, max = 20) String nickname,
        @NotNull @Size(max = 15) String profileAddress,
        @NotNull String introduction
) {

    public MemberInfoUpdateInfo toInfo() {
        return new MemberInfoUpdateInfo(nickname, profileAddress, introduction);
    }
}
