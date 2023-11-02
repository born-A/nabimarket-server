package org.prgrms.nabimarketbe.domain.user.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserResponseDTO(
    Long userId,
    String accountId,
    String nickName,
    Role role,
    LocalDateTime createdDate,
    LocalDateTime modifiedDate
) {
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
            .userId(user.getUserId())
            .accountId(user.getAccountId())
            .nickName(user.getNickname())
            .role(user.getRole())
            .createdDate(user.getCreatedDate())
            .modifiedDate(user.getModifiedDate())
            .build();
    }
}
