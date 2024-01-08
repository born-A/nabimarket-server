package org.prgrms.nabimarketbe.user.service;

import java.util.Objects;

import org.prgrms.nabimarketbe.jwt.provider.JwtProvider;
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

    public boolean isEqual(
        String token,
        Long userId
    ) {
        Long tokenUserId = parseToken(token);

        return Objects.equals(tokenUserId, userId);
    }

    public boolean isEqual(
        Long userId,
        Long ownerUserId
    ) {
        return Objects.equals(userId, ownerUserId);
    }
}
