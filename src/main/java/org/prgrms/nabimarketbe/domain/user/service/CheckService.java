package org.prgrms.nabimarketbe.domain.user.service;

import java.util.Objects;

import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckService {
	private final JwtProvider jwtProvider;

	public Long parseToken(String token) {
		Claims claims = jwtProvider.parseClaims(token);
		Long userId = Long.valueOf(claims.getSubject());

		return userId;
	}

	public void checkToken(String token, Long userId) {
		Long tokenUserId = parseToken(token);

		if(!Objects.equals(tokenUserId, userId)) {
			throw new RuntimeException("token과 userId가 일치하지 않습니다.");
		}
	}
}
