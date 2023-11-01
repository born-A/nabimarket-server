package org.prgrms.nabimarketbe.domain.user.dto.request;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

@Builder
public record UserSignInRequestDto(String accountId, String nickname, String provider) {
    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .provider(provider)
                .role(Role.USER)
                .build();
    }
}

