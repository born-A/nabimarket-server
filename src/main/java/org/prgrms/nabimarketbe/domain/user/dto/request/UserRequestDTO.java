package org.prgrms.nabimarketbe.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.nabimarketbe.domain.user.entity.User;

@Getter
@Builder
public class UserRequestDTO {
    private String imageUrl;

    private String nickname;

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .build();
    }
}
