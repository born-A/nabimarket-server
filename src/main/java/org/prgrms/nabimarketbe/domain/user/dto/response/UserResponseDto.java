package org.prgrms.nabimarketbe.domain.user.dto.response;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long userId;

    private final String nickName;

    private final Role role;

    private final LocalDateTime modifiedDate;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickname();
        this.role = user.getRole();
        this.modifiedDate = user.getModifiedDate();
    }
}
