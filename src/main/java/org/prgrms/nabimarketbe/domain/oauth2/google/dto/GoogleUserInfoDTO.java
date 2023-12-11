package org.prgrms.nabimarketbe.domain.oauth2.google.dto;

import org.prgrms.nabimarketbe.domain.user.entity.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

public record GoogleUserInfoDTO(
    String id
) {
    private static final String PROVIDER = "GOOGLE";

    public User toEntity(String nickName) {
        return User.builder()
            .nickname(nickName)
            .provider(PROVIDER)
            .accountId(id)
            .role(Role.USER.getKey())
            .build();
    }
}
