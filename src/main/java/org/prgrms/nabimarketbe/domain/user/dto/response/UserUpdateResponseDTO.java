package org.prgrms.nabimarketbe.domain.user.dto.response;

import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserUpdateResponseDTO(
	Long userId,
	String nickname,
	String profileUrl
) {
	public static UserUpdateResponseDTO from(User user) {
		return UserUpdateResponseDTO.builder()
			.userId(user.getUserId())
			.nickname(user.getNickname())
			.profileUrl(user.getImageUrl())
			.build();
	}
}
