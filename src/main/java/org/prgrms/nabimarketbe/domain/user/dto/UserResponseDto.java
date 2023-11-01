package org.prgrms.nabimarketbe.domain.user.dto;

import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserResponseDto(
    Long userId,
    String nickname,
    String imageUrl
) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .imageUrl(user.getImageUrl())
            .build();
    }
}
