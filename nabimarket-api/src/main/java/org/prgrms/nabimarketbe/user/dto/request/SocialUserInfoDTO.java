package org.prgrms.nabimarketbe.user.dto.request;

import javax.validation.constraints.NotBlank;

import org.prgrms.nabimarketbe.user.entity.Role;
import org.prgrms.nabimarketbe.user.entity.User;

import lombok.Builder;

@Builder
public record SocialUserInfoDTO(
    @NotBlank(message = "accountId가 비어있으면 안됩니다.")
    String accountId,
    @NotBlank(message = "provider가 비어있으면 안됩니다.")
    String provider
) {
    public User toEntity(String nickName) {
        return User.builder()
            .accountId(accountId)
            .nickname(nickName)
            .provider(provider)
            .role(Role.USER.getKey())
            .build();
    }
}

