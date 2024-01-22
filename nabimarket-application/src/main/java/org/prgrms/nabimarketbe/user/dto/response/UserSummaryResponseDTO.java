package org.prgrms.nabimarketbe.user.dto.response;


import org.prgrms.nabimarketbe.user.entity.User;

import lombok.Builder;

@Builder
public record UserSummaryResponseDTO(
    Long userId,
    String nickname,
    String imageUrl
) {
    public static UserSummaryResponseDTO from(User user) {
        return UserSummaryResponseDTO.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .imageUrl(user.getImageUrl())
            .build();
    }
}
