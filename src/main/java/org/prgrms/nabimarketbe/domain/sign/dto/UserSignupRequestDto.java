package org.prgrms.nabimarketbe.domain.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {
    private String nickname;
    private String provider;

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
