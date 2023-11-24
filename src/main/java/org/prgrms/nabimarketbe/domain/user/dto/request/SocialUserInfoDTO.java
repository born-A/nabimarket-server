package org.prgrms.nabimarketbe.domain.user.dto.request;

import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record SocialUserInfoDTO(
    String accountId,
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

