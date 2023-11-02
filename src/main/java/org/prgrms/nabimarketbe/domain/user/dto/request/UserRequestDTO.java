package org.prgrms.nabimarketbe.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.nabimarketbe.domain.user.entity.User;

@Getter
@Builder
public class UserRequestDTO {
    private String email;

    private String nickName;

    public User toEntity() {
        return User.builder()
                .nickname(nickName)
                .build();
    }
}
