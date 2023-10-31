package org.prgrms.nabimarketbe.domain.user.dto.sign;

import java.util.Collections;

import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserSignupRequestDto(String nickname, String provider) {
	public User toEntity() {
		return User.builder()
			.nickname(nickname)
			.provider(provider)
			.roles(Collections.singletonList("ROLE_USER"))
			.build();
	}
}
