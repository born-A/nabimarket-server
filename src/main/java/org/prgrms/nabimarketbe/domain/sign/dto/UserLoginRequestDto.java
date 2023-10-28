package org.prgrms.nabimarketbe.domain.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequestDto {
    private String nickname;

    public User toUser() {
        return User.builder()
                .nickname(nickname)
                .build();
    }
}
