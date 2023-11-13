package org.prgrms.nabimarketbe.domain.user.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.user.entity.User;

@Builder
public record UserSummaryResponseDTO(
	Long userId,
	String nickname,
	String profileUrl
) {
	public static UserSummaryResponseDTO from(User user) {
		return UserSummaryResponseDTO.builder()
			.userId(user.getUserId())
			.nickname(user.getNickname())
			.profileUrl(user.getImageUrl())
			.build();
	}
}
