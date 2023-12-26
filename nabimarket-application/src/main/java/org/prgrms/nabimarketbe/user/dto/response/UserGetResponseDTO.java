package org.prgrms.nabimarketbe.user.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.user.entity.User;

import lombok.Builder;

@Builder
public record UserGetResponseDTO(
    Long userId,
    String accountId,
    String nickname,
    String imageUrl,
    String role,
    LocalDateTime createdDate,
    LocalDateTime modifiedDate
) {
    public static UserGetResponseDTO from(User user) {
        return UserGetResponseDTO.builder()
            .userId(user.getUserId())
            .accountId(user.getAccountId())
            .nickname(user.getNickname())
            .imageUrl(user.getImageUrl())
            .role(user.getRole())
            .createdDate(user.getCreatedDate())
            .modifiedDate(user.getModifiedDate())
            .build();
    }
}
