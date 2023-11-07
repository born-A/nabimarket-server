package org.prgrms.nabimarketbe.domain.user.dto.request;

import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserSignInRequestDTO(
        String accountId,
        String nickname,
        String provider
) {
    public User toEntity() {
        return User.builder()
                .accountId(accountId)
                .nickname(nickname)
                .provider(provider)
                .role(Role.USER.getKey())
                .build();
    }

    public User toEntity(String nickName) {
        return User.builder()
                .accountId(accountId)
                .nickname(nickName)
                .provider(provider)
                .role(Role.USER.getKey())
                .build();
    }
}

