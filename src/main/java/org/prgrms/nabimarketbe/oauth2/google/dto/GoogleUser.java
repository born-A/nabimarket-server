package org.prgrms.nabimarketbe.oauth2.google.dto;

import java.util.List;

import org.prgrms.nabimarketbe.domain.user.entity.User;

public record GoogleUser(
	String id,
	String email,
	Boolean verifiedEmail,
	String name,
	String givenName,
	String familyName,
	String picture,
	String locale
) {

	private static final String PROVIDER = "GOOGLE";

	public User toEntity(String nickName) {
		return User.builder()
			.nickname(nickName)
			.email(email)
			.imageUrl(picture)
			.provider(PROVIDER)
			.roles(List.of("ROLE_USER"))
			.build();
	}
}
