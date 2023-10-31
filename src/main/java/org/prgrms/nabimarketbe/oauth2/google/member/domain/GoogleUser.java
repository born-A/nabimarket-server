package org.prgrms.nabimarketbe.oauth2.google.member.domain;

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
}
